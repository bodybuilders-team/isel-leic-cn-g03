package pt.isel.cn.landmarks.server;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import landmarks.GetIdentifiedPhotosRequest;
import landmarks.GetIdentifiedPhotosResponse;
import landmarks.GetResultsRequest;
import landmarks.GetResultsResponse;
import landmarks.IdentifiedPhoto;
import landmarks.ImageMap;
import landmarks.LandmarksServiceGrpc;
import landmarks.SubmitImageRequest;
import landmarks.SubmitImageResponse;
import pt.isel.cn.landmarks.server.service.Service;
import pt.isel.cn.landmarks.server.service.dtos.GetResultsOutput;
import pt.isel.cn.landmarks.server.service.dtos.IdentifiedPhotoOutput;
import pt.isel.cn.landmarks.server.service.exceptions.ImageSubmissionException;
import pt.isel.cn.landmarks.server.service.exceptions.InvalidConfidenceThresholdException;
import pt.isel.cn.landmarks.server.service.exceptions.MapImageRetrievalException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotFoundException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotProcessedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Server for the Landmarks GRPC service following {@link LandmarksServiceGrpc} contract.
 */
public class LandmarksServer extends LandmarksServiceGrpc.LandmarksServiceImplBase {
    private final Service service;

    public LandmarksServer(Service service) {
        this.service = service;
    }

    @Override
    public ImageRequestStreamObserver submitImage(
            StreamObserver<landmarks.SubmitImageResponse> responseObserver) {
        return new ImageRequestStreamObserver(responseObserver);
    }

    @Override
    public void getResults(GetResultsRequest request,
                           StreamObserver<landmarks.GetResultsResponse> responseObserver) {
        String requestId = request.getRequestId();

        LandmarksServerLogger.logger.info(String.format(
                "Received request for results of request with id %s", requestId
        ));

        try {
            GetResultsOutput getResultsOutput = service.getResults(requestId);

            GetResultsResponse.Builder getResultsResponseBuilder = GetResultsResponse.newBuilder()
                    .addAllLandmarks(getResultsOutput.getLandmarkMetadataList().stream().map(landmarkMetadata ->
                            landmarks.Landmark.newBuilder()
                                    .setName(landmarkMetadata.getName())
                                    .setLatitude(landmarkMetadata.getLocation().getLatitude())
                                    .setLongitude(landmarkMetadata.getLocation().getLongitude())
                                    .setConfidence(landmarkMetadata.getConfidence())
                                    .build()
                    ).collect(Collectors.toList()));

            if (getResultsOutput.getMapImage() == null) {
                responseObserver.onNext(getResultsResponseBuilder.build());
                responseObserver.onCompleted();
                return;
            }

            responseObserver.onNext(getResultsResponseBuilder
                    .setMap(ImageMap.newBuilder()
                            .setImageData(ByteString.copyFrom(getResultsOutput.getMapImage()))
                            .build())
                    .build()
            );
            responseObserver.onCompleted();
        } catch (RequestNotFoundException e) {
            LandmarksServerLogger.logger.info(e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(String.format(
                    "Request with id %s not found. Please make sure you have entered a valid request id or create a new request.",
                    requestId
            )).asException());
        } catch (RequestNotProcessedException e) {
            LandmarksServerLogger.logger.info(e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(String.format(
                    "Request with id %s didn't finish processing yet. Please try again later.",
                    requestId
            )).asException());
        } catch (MapImageRetrievalException e) {
            LandmarksServerLogger.logger.severe(e.getMessage());
            // TODO error or just return the landmarks?
            responseObserver.onError(Status.INTERNAL.withDescription(String.format(
                    "Error retrieving landmark map from request with id %s. Please try again later or contact support if the problem persists.",
                    requestId
            )).asException());
        }
    }

    @Override
    public void getIdentifiedPhotos(GetIdentifiedPhotosRequest request,
                                    StreamObserver<landmarks.GetIdentifiedPhotosResponse> responseObserver) {
        float confidenceThreshold = request.getConfidenceThreshold();

        LandmarksServerLogger.logger.info(String.format(
                "Received request for identified photos with confidence threshold of %s", confidenceThreshold
        ));

        try {
            List<IdentifiedPhotoOutput> identifiedPhotos = service.getIdentifiedPhotos(confidenceThreshold);

            responseObserver.onNext(
                    GetIdentifiedPhotosResponse.newBuilder()
                            .addAllIdentifiedPhotos(identifiedPhotos.stream().map(identifiedPhoto -> IdentifiedPhoto.newBuilder()
                                    .setPhotoName(identifiedPhoto.getPhotoName())
                                    .setLandmarkName(identifiedPhoto.getLandmarkName())
                                    .setConfidence(identifiedPhoto.getConfidence())
                                    .build()).collect(Collectors.toList()))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (InvalidConfidenceThresholdException e) {
            LandmarksServerLogger.logger.info(e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(String.format(
                    "Confidence threshold of %f is invalid. Must be between 0 and 1.", confidenceThreshold
            )).asException());
        }
    }

    /**
     * Handles the transfer of the image, storing the bytes the client sends through the stream and then submitting the
     * image for processing.
     */
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

            LandmarksServerLogger.logger.info(String.format("Submitting image for request %s", requestId));
            try {
                service.submitImage(requestId, imageBytes.toByteArray(), photoName);

                LandmarksServerLogger.logger.info(String.format(
                        "Successfully submitted image with request %s", requestId
                ));
            } catch (ImageSubmissionException e) {
                LandmarksServerLogger.logger.info(String.format(
                        "Error during image submission of request %s", requestId
                ));
            }
        }
    }
}