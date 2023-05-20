package pt.isel.cn.landmarks.server;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import landmarks.GetIdentifiedPhotosResponse;
import landmarks.GetResultsResponse;
import landmarks.IdentifiedPhoto;
import landmarks.ImageMap;
import landmarks.LandmarksServiceGrpc;
import landmarks.SubmitImageRequest;
import landmarks.SubmitImageResponse;
import pt.isel.cn.landmarks.server.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.server.domain.RequestMetadata;
import pt.isel.cn.landmarks.server.service.images.ImageService;
import pt.isel.cn.landmarks.server.service.maps.MapService;
import pt.isel.cn.landmarks.server.storage.metadata.MetadataStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Server for the Landmarks GRPC service following {@link LandmarksServiceGrpc} contract.
 */
public class LandmarksServer extends LandmarksServiceGrpc.LandmarksServiceImplBase {
    private final ImageService imageService;
    private final MapService mapService;
    private final MetadataStorage metadataStorage;

    public LandmarksServer(ImageService imageService, MapService mapService, MetadataStorage metadataStorage) {
        this.imageService = imageService;
        this.mapService = mapService;
        this.metadataStorage = metadataStorage;
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

        LandmarksServerLogger.logger.info(String.format(
                "Received request for results of request with id %s", requestId
        ));

        Optional<RequestMetadata> requestOptional = metadataStorage.getRequestMetadata(requestId);

        if (requestOptional.isEmpty()) {
            LandmarksServerLogger.logger.info(String.format("Request with id %s not found", requestId));
            responseObserver.onError(Status.NOT_FOUND.withDescription(String.format(
                    "Request with id %s not found", requestId
            )).asException());
            return;
        }

        RequestMetadata requestMetadata = requestOptional.get();
        if (!requestMetadata.getStatus().equals("DONE")) {
            LandmarksServerLogger.logger.info(String.format("Request with id %s didn't finish processing yet", requestId));
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(String.format(
                    "Request with id %s didn't finish processing yet. Try again later", requestId
            )).asException());
            return;
        }

        List<LandmarkMetadata> landmarkMetadataList = requestMetadata.getLandmarks();

        GetResultsResponse.Builder getResultsResponseBuilder = GetResultsResponse.newBuilder()
                .addAllLandmarks(landmarkMetadataList.stream().map(landmarkMetadata ->
                        landmarks.Landmark.newBuilder()
                                .setName(landmarkMetadata.getName())
                                .setLatitude(landmarkMetadata.getLocation().getLatitude())
                                .setLongitude(landmarkMetadata.getLocation().getLongitude())
                                .setConfidence(landmarkMetadata.getConfidence())
                                .build()
                ).collect(Collectors.toList()));

        if (landmarkMetadataList.isEmpty()) {
            responseObserver.onNext(getResultsResponseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        LandmarkMetadata biggestConfidenceLandmark = landmarkMetadataList.stream()
                .max(Comparator.comparingDouble(LandmarkMetadata::getConfidence))
                .get();

        byte[] mapImage = mapService.getMapImage(biggestConfidenceLandmark.getMapBlobName());
        if (mapImage == null) { // TODO if one fails retrieve another?
            LandmarksServerLogger.logger.severe(String.format(
                    "Error retrieving map from landmark with name %s", biggestConfidenceLandmark.getMapBlobName()
            ));

            responseObserver.onError(Status.INTERNAL.withDescription(String.format(
                    "Error retrieving map from landmark with name %s", biggestConfidenceLandmark.getMapBlobName()
            )).asException());
            return;
        }

        responseObserver.onNext(getResultsResponseBuilder
                .setMap(ImageMap.newBuilder()
                        .setImageData(ByteString.copyFrom(mapImage))
                        .build())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getIdentifiedPhotos(landmarks.GetIdentifiedPhotosRequest request,
                                    io.grpc.stub.StreamObserver<landmarks.GetIdentifiedPhotosResponse> responseObserver) {
        float confidenceThreshold = request.getConfidenceThreshold();

        LandmarksServerLogger.logger.info(String.format(
                "Received request for identified photos with confidence threshold of %s", confidenceThreshold
        ));

        if (confidenceThreshold < 0 || confidenceThreshold > 1) {
            LandmarksServerLogger.logger.info(String.format(
                    "Confidence threshold of %s is invalid.", confidenceThreshold
            ));
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(String.format(
                    "Confidence threshold of %s is invalid. Must be between 0 and 1.", confidenceThreshold
            )).asException());
            return;
        }

        List<IdentifiedPhoto> identifiedPhotos = metadataStorage.getAllRequestMetadata()
                .stream()
                .filter(requestMetadata -> requestMetadata.getLandmarks().stream()
                        .anyMatch(landmarkMetadata -> landmarkMetadata.getConfidence() >= confidenceThreshold))
                .map(requestMetadata -> {
                            @SuppressWarnings("OptionalGetWithoutIsPresent")
                            LandmarkMetadata biggestConfidenceLandmark = requestMetadata.getLandmarks().stream()
                                    .filter(landmarkMetadata -> landmarkMetadata.getConfidence() >= confidenceThreshold)
                                    .max(Comparator.comparingDouble(LandmarkMetadata::getConfidence))
                                    .get();

                            return IdentifiedPhoto.newBuilder()
                                    .setPhotoName(requestMetadata.getPhotoName())
                                    .setLandmarkName(biggestConfidenceLandmark.getName())
                                    .setConfidence(biggestConfidenceLandmark.getConfidence())
                                    .build();
                        }
                ).collect(Collectors.toList());

        responseObserver.onNext(
                GetIdentifiedPhotosResponse.newBuilder()
                        .addAllIdentifiedPhotos(identifiedPhotos)
                        .build()
        );
        responseObserver.onCompleted();
    }

    private class ImageRequestStreamObserver implements StreamObserver<SubmitImageRequest> {

        String photoName;
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
                photoName = submitImageRequest.getPhotoName();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            LandmarksServerLogger.logger.info("Error: " + throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            String requestId = UUID.randomUUID().toString();

            LandmarksServerLogger.logger.info(String.format(
                    "Finished receiving image bytes: Assigned new request %s", requestId
            ));

            responseObserver.onNext(SubmitImageResponse.newBuilder()
                    .setRequestId(requestId)
                    .build());
            responseObserver.onCompleted();

            LandmarksServerLogger.logger.info(String.format("Uploading image for request %s", requestId));
            try {
                String blobName = imageService.uploadImage(imageBytes.toByteArray());

                imageService.notifyImageUploaded(requestId, photoName, blobName);

                LandmarksServerLogger.logger.info(String.format(
                        "Successfully uploaded image with request %s", requestId
                ));
            } catch (IOException | ExecutionException | InterruptedException e) {
                LandmarksServerLogger.logger.info(String.format(
                        "Error during image upload of request %s", requestId
                ));
            }
        }
    }
}