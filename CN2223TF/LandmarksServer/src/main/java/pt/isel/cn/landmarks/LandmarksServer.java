package pt.isel.cn.landmarks;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import landmarks.LandmarksServiceGrpc;
import landmarks.SubmitImageRequest;
import landmarks.SubmitImageResponse;
import pt.isel.cn.landmarks.service.ImageService;
import pt.isel.cn.landmarks.service.ImageServiceImpl;
import pt.isel.cn.landmarks.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.storage.data.DataStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class LandmarksServer extends LandmarksServiceGrpc.LandmarksServiceImplBase {
    private static final int svcPort = 8000;

    private final ImageService imageService;

    public LandmarksServer(ImageService imageService) {
        this.imageService = imageService;
    }

    public static void main(String[] args) {
        try {
            DataStorage dataStorage = new CloudDataStorage();
            ImageService imageService = new ImageServiceImpl(dataStorage);

            Server svc = ServerBuilder.forPort(svcPort)
                    .addService(new LandmarksServer(imageService))
                    .build()
                    .start();
            System.out.println("LandmarksServer started on port " + svcPort);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            svc.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ImageRequestStreamObserver implements StreamObserver<SubmitImageRequest> {

        ByteArrayOutputStream imageBytes;
        StreamObserver<SubmitImageResponse> responseObserver;

        public ImageRequestStreamObserver(StreamObserver<SubmitImageResponse> responseObserver) {
            this.imageBytes = new ByteArrayOutputStream();
            this.responseObserver = responseObserver;
        }

        @Override
        public void onNext(SubmitImageRequest submitImageRequest) {
            try {
                imageBytes.write(submitImageRequest.getImageData().toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Error: " + throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            String requestId = UUID.randomUUID().toString();

            responseObserver.onNext(SubmitImageResponse.newBuilder()
                    .setRequestId(requestId)
                    .build());
            responseObserver.onCompleted();

            try {
                String blobName = imageService.uploadImage(imageBytes.toByteArray());

                imageService.notifyImageUploaded(requestId, blobName);
            } catch (IOException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ImageRequestStreamObserver submitImage(
            StreamObserver<landmarks.SubmitImageResponse> responseObserver) {
        return new ImageRequestStreamObserver(responseObserver);
    }

    @Override
    public void getResults(landmarks.GetResultsRequest request,
                           io.grpc.stub.StreamObserver<landmarks.GetResultsResponse> responseObserver) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void getIdentifiedPhotos(landmarks.GetIdentifiedPhotosRequest request,
                                    io.grpc.stub.StreamObserver<landmarks.GetIdentifiedPhotosResponse> responseObserver) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}