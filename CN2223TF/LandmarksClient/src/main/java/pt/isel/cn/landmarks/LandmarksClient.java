package pt.isel.cn.landmarks;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import landmarks.GetIdentifiedPhotosRequest;
import landmarks.GetIdentifiedPhotosResponse;
import landmarks.GetResultsRequest;
import landmarks.GetResultsResponse;
import landmarks.IdentifiedPhoto;
import landmarks.Landmark;
import landmarks.LandmarksContractGrpc;
import landmarks.SubmitImageRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class LandmarksClient {

    private static final int SVC_PORT = 8000;
    private static final String SVC_IP = "localhost";

    private static final int BLOCK_SIZE = 4096; // 4KB buffer

    public static LandmarksContractGrpc.LandmarksContractStub stub;
    public static LandmarksContractGrpc.LandmarksContractBlockingStub blockingStub;
    private static ManagedChannel channel;


    /**
     * Entry point for the Landmarks client.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // TODO: IP lookup

        channel = ManagedChannelBuilder.forAddress(SVC_IP, SVC_PORT)
                .usePlaintext()
                .build();
        stub = LandmarksContractGrpc.newStub(channel);
        blockingStub = LandmarksContractGrpc.newBlockingStub(channel);

        boolean end = false;
        while (!end) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        submitImage();
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
            } catch (Exception ex) {
                System.out.println("Error on call:" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Displays the menu and reads the user option.
     *
     * @return The user option.
     */
    static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("########## Landmarks Menu ##########");
            System.out.println("Options for the Landmarks service:");
            System.out.println(" 0: Submit Image");
            System.out.println(" 1: Get Results");
            System.out.println(" 2: Get Identified Photos");
            System.out.println(" 3: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!(option >= 0 && option <= 3));
        return option;
    }

    /**
     * Submits an image to the service.
     * <p>
     * Asks the user for the image path and sends the image data to the service.
     * The image data is sent as a stream of blocks.
     */
    static void submitImage() {
        System.out.println("########## Submit Image ##########");
        System.out.print("Enter the image path: ");
        Scanner scan = new Scanner(System.in);
        String filePath = scan.nextLine();

        // Create a stream observer to handle the response
        SubmitImageResponseObserver responseObserver = new SubmitImageResponseObserver();

        // Send the image data as a stream of blocks
        StreamObserver<SubmitImageRequest> requestObserver = stub.submitImage(responseObserver);

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[BLOCK_SIZE];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                // Create a new request message with the image block data
                SubmitImageRequest request = SubmitImageRequest.newBuilder()
                        .setImageData(ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();

                // Send the request to the server
                requestObserver.onNext(request);
            }
        } catch (IOException e) {
            requestObserver.onError(e);
        }

        // Mark the end of the request
        requestObserver.onCompleted();
    }

    /**
     * Gets the results for a request.
     * <p>
     * Asks the user for the request id and prints the results.
     * The results are printed in the following format:
     * Landmark Name: <landmark name>
     * Latitude: <latitude>
     * Longitude: <longitude>
     * Confidence: <confidence>
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

        // Process the response
        for (Landmark landmark : response.getLandmarksList()) {
            System.out.println("Landmark Name: " + landmark.getName());
            System.out.println("Latitude: " + landmark.getLatitude());
            System.out.println("Longitude: " + landmark.getLongitude());
            System.out.println("Confidence: " + landmark.getConfidence());
            System.out.println();
        }
    }

    /**
     * Gets the identified photos for a confidence threshold.
     * <p>
     * Asks the user for the confidence threshold and prints the identified photos.
     * The identified photos are printed in the following format:
     * Photo Name: <photo name>
     * Landmark Name: <landmark name>
     */
    public static void getIdentifiedPhotos() {
        System.out.println("########## Get Identified Photos ##########");
        System.out.print("Enter the confidence threshold: ");
        Scanner scan = new Scanner(System.in);
        float confidenceThreshold = scan.nextFloat();

        GetIdentifiedPhotosRequest request = GetIdentifiedPhotosRequest.newBuilder()
                .setConfidenceThreshold(confidenceThreshold)
                .build();

        GetIdentifiedPhotosResponse response = blockingStub.getIdentifiedPhotos(request);

        // Process the response
        for (IdentifiedPhoto identifiedPhoto : response.getIdentifiedPhotosList()) {
            System.out.println("Photo Name: " + identifiedPhoto.getPhotoName());
            System.out.println("Landmark Name: " + identifiedPhoto.getLandmarkName());
            System.out.println();
        }
    }
}
