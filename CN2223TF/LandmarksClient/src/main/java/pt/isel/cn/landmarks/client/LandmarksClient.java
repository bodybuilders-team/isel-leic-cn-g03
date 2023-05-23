package pt.isel.cn.landmarks.client;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import landmarks.GetIdentifiedPhotosRequest;
import landmarks.GetIdentifiedPhotosResponse;
import landmarks.GetResultsRequest;
import landmarks.GetResultsResponse;
import landmarks.IdentifiedPhoto;
import landmarks.Landmark;
import landmarks.LandmarksServiceGrpc;
import landmarks.SubmitPhotoRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Console client for the Landmarks service.
 */
public class LandmarksClient {

    private static final int SVC_PORT = 8000;
    private static final String SVC_IP = "localhost";

    private static final String MAP_DOWNLOAD_DIRECTORY = "downloadedMaps";

    private static final int BLOCK_SIZE = 4096; // 4KB buffer

    private static  LandmarksServiceGrpc.LandmarksServiceStub stub;
    private static LandmarksServiceGrpc.LandmarksServiceBlockingStub blockingStub;

    /**
     * Entry point for the Landmarks client.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO: IP lookup

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SVC_IP, SVC_PORT)
                .usePlaintext()
                .build();
        stub = LandmarksServiceGrpc.newStub(channel);
        blockingStub = LandmarksServiceGrpc.newBlockingStub(channel);

        boolean end = false;
        while (!end) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        submitPhoto();
                        break;
                    case 1:
                        getResults();
                        break;
                    case 2:
                        getIdentifiedPhotos();
                        break;
                    case 3:
                        end = true;
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            } catch (StatusRuntimeException ex) {
                System.out.println("Error on call: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Displays the menu and reads the user option.
     *
     * @return the user option
     */
    static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("########## Landmark Recognition System ##########");
            System.out.println(" 0: Submit Photo");
            System.out.println(" 1: Get Results");
            System.out.println(" 2: Get Identified Photos");
            System.out.println(" 3: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!(option >= 0 && option <= 3));
        return option;
    }

    /**
     * Submits a photo to the service.
     * <p>
     * Asks the user for the photo name and photo path and sends the photo to the service.
     * The photo is sent as a stream of blocks.
     */
    static void submitPhoto() {
        Scanner scan = new Scanner(System.in);

        System.out.println("########## Submit Photo ##########");
        System.out.print("Enter the photo name: ");
        String photoName = scan.nextLine();
        System.out.print("Enter the photo path: ");
        String filePath = scan.nextLine();

        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("Photo does not exist in the provided path.");
            return;
        }

        // Create a stream observer to handle the response
        SubmitPhotoResponseObserver responseObserver = new SubmitPhotoResponseObserver();

        // Send the photo as a stream of blocks
        StreamObserver<SubmitPhotoRequest> requestObserver = stub.submitPhoto(responseObserver);

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[BLOCK_SIZE];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                // Create a new request message with the photo block data
                SubmitPhotoRequest request = SubmitPhotoRequest.newBuilder()
                        .setPhotoName(photoName)
                        .setPhoto(ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();

                // Send the request to the server
                requestObserver.onNext(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
            requestObserver.onError(e);
            return;
        }

        // Mark the end of the request
        requestObserver.onCompleted();
    }

    /**
     * Gets the results for a request.
     * <p>
     * Asks the user for the request id and prints the results and creates a file with the received map image.
     * <p>
     * The results are printed in the following format:
     * <p>
     * Landmark Name: [landmark_name]
     * <p>
     * Latitude: [latitude]
     * <p>
     * Longitude: [longitude]
     * <p>
     * Confidence: [confidence]
     */
    public static void getResults() {
        System.out.println("########## Get Results ##########");
        System.out.print("Enter the request id: ");
        Scanner scan = new Scanner(System.in);
        String requestId = scan.nextLine();

        GetResultsRequest request = GetResultsRequest.newBuilder()
                .setRequestId(requestId)
                .build();

        GetResultsResponse response = blockingStub.getResults(request);

        if (response.getLandmarksList().isEmpty()) {
            System.out.println("No landmarks found for this request!");
            return;
        }

        // Process the response
        for (Landmark landmark : response.getLandmarksList()) {
            System.out.println("Landmark Name: " + landmark.getName());
            System.out.println("Latitude: " + landmark.getLatitude());
            System.out.println("Longitude: " + landmark.getLongitude());
            System.out.println("Confidence: " + landmark.getConfidence());
            System.out.println();
        }

        try {
            Path directoryPath = Paths.get(MAP_DOWNLOAD_DIRECTORY);
            if (!Files.exists(directoryPath))
                Files.createDirectories(directoryPath);

            // Check if the file exists and create it if not
            Path downloadTo = directoryPath.resolve(String.format("map-%s.png", requestId));
            if (!Files.exists(downloadTo))
                Files.createFile(downloadTo);

            PrintStream writeTo = new PrintStream(Files.newOutputStream(downloadTo));

            writeTo.write(response.getMap().getImageData().toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the identified photos for a confidence threshold.
     * <p>
     * Asks the user for the confidence threshold and prints the identified photos.
     * The identified photos are printed in the following format:
     * <p>
     * Photo Name: [photo_name]
     * <p>
     * Landmark Name: [landmark_name]
     * <p>
     * Confidence: [confidence]
     */
    public static void getIdentifiedPhotos() {
        System.out.println("########## Get Identified Photos ##########");
        System.out.print("Enter the confidence threshold: ");
        Scanner scan = new Scanner(System.in);
        float confidenceThreshold = scan.nextFloat();

        if (confidenceThreshold < 0 || confidenceThreshold > 1) {
            System.out.println("Invalid confidence threshold! Must be between 0 and 1.");
            return;
        }

        GetIdentifiedPhotosRequest request = GetIdentifiedPhotosRequest.newBuilder()
                .setConfidenceThreshold(confidenceThreshold)
                .build();

        GetIdentifiedPhotosResponse response = blockingStub.getIdentifiedPhotos(request);

        if (response.getIdentifiedPhotosList().isEmpty()) {
            System.out.printf("No identified photos found within the confidence threshold of %s!\n", confidenceThreshold);
            return;
        }

        System.out.printf("Identified Photos within the confidence threshold of %s:\n", confidenceThreshold);

        // Process the response
        for (IdentifiedPhoto identifiedPhoto : response.getIdentifiedPhotosList()) {
            System.out.println("Photo Name: " + identifiedPhoto.getPhotoName());
            System.out.println("Landmark Name: " + identifiedPhoto.getLandmarkName());
            System.out.println("Confidence: " + identifiedPhoto.getConfidence());
            System.out.println();
        }
    }
}
