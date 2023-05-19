package pt.isel.cn.landmarks.server;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import landmarks.GetResultsResponse;
import landmarks.ImageMap;
import landmarks.LandmarksServiceGrpc;
import landmarks.SubmitImageRequest;
import landmarks.SubmitImageResponse;
import pt.isel.cn.landmarks.server.domain.Landmark;
import pt.isel.cn.landmarks.server.domain.Request;
import pt.isel.cn.landmarks.server.service.ImageService;
import pt.isel.cn.landmarks.server.service.ImageServiceImpl;
import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.server.storage.data.GoogleCloudCloudDataStorage;
import pt.isel.cn.landmarks.server.storage.metadata.FirestoreMetadataStorage;
import pt.isel.cn.landmarks.server.storage.metadata.MetadataStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LandmarksServer extends LandmarksServiceGrpc.LandmarksServiceImplBase {
    private static final int svcPort = 8000;

    private final ImageService imageService;
    private final MetadataStorage metadataStorage;

    public LandmarksServer(ImageService imageService, MetadataStorage metadataStorage) {
        this.imageService = imageService;
        this.metadataStorage = metadataStorage;
    }

    public static void main(String[] args) {
        CloudDataStorage cloudDataStorage = new GoogleCloudCloudDataStorage(StorageOptions.getDefaultInstance().getService());
        MetadataStorage metadataStorage = new FirestoreMetadataStorage(FirestoreOptions.getDefaultInstance().getService());
        ImageService imageService = new ImageServiceImpl(cloudDataStorage);

        try {
            Server svc = ServerBuilder.forPort(svcPort)
                    .addService(new LandmarksServer(imageService, metadataStorage))
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

    @Override
    public ImageRequestStreamObserver submitImage(
            StreamObserver<landmarks.SubmitImageResponse> responseObserver) {
        return new ImageRequestStreamObserver(responseObserver);
    }

    @Override
    public void getResults(landmarks.GetResultsRequest request,
                           io.grpc.stub.StreamObserver<landmarks.GetResultsResponse> responseObserver) {
        String requestId = request.getRequestId();

        Optional<Request> requestOptional = metadataStorage.getRequestMetadata(requestId);

        if (requestOptional.isEmpty()) {
            responseObserver.onError(new RuntimeException(String.format("Request with id %s not found", requestId)));
            return;
        }

        Request requestMetadata = requestOptional.get();
        if (!requestMetadata.getStatus().equals("DONE")) {
            responseObserver.onError(new RuntimeException(String.format("Request with id %s didn't finish processing", requestId)));
            return;
        }

        List<Landmark> landmarkList = List.of(requestMetadata.getLandmarks());

        responseObserver.onNext(GetResultsResponse.newBuilder()
                .setMap(ImageMap.newBuilder()
                        .setImageData(ByteString.copyFrom(landmarkList.get(0).getMap()))
                        .build())
                .addAllLandmarks(landmarkList.stream().map(landmark ->
                        landmarks.Landmark.newBuilder()
                                .setConfidence(landmark.getConfidence())
                                .setLatitude(landmark.getLocation().getLatitude())
                                .setLongitude(landmark.getLocation().getLongitude())
                                .setName(landmark.getName())
                                .build()
                ).collect(Collectors.toList()))
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getIdentifiedPhotos(landmarks.GetIdentifiedPhotosRequest request,
                                    io.grpc.stub.StreamObserver<landmarks.GetIdentifiedPhotosResponse> responseObserver) {
        float confidenceThreshold = request.getConfidenceThreshold();

        /*List<IdentifiedPhoto> identifiedPhotos = metadataStorage.getRequestsMetadataByConfidence(confidenceThreshold)
                .stream().map(request ->
                        IdentifiedPhoto.newBuilder()
                                .setLandmarkName(request.getLandmarks())
                                .setPhotoName(request.)
                                .build()
                ).collect(Collectors.toList());

        responseObserver.onNext(
                GetIdentifiedPhotosResponse.newBuilder()
                        .addAllIdentifiedPhotos(identifiedPhotos)
                        .build()
        );
        responseObserver.onCompleted();*/
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
                responseObserver.onError(new RuntimeException(
                        String.format("Error during image upload of request with id %s", requestId))
                );
            }
        }
    }
}