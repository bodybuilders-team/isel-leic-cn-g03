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
import landmarks.SubmitPhotoRequest;
import landmarks.SubmitPhotoResponse;
import pt.isel.cn.landmarks.server.service.Service;
import pt.isel.cn.landmarks.server.service.dtos.GetResultsOutput;
import pt.isel.cn.landmarks.server.service.dtos.IdentifiedPhotoOutput;
import pt.isel.cn.landmarks.server.service.exceptions.PhotoSubmissionException;
import pt.isel.cn.landmarks.server.service.exceptions.InvalidConfidenceThresholdException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotFoundException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotProcessedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Server for the Landmarks gRPC service following {@link LandmarksServiceGrpc} contract.
 */
public class LandmarksServer extends LandmarksServiceGrpc.LandmarksServiceImplBase {
    private final Service service;

    public LandmarksServer(Service service) {
        this.service = service;
    }

    @Override
    public PhotoRequestStreamObserver submitPhoto(
            StreamObserver<landmarks.SubmitPhotoResponse> responseObserver) {
        return new PhotoRequestStreamObserver(responseObserver);
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
                LandmarksServerLogger.logger.severe("Error retrieving map image");

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
     * Handles the transfer of the photo, storing the bytes the client sends through the stream and then submitting the
     * photo for processing.
     */
    private class PhotoRequestStreamObserver implements StreamObserver<SubmitPhotoRequest> {

        private String photoName;
        private final ByteArrayOutputStream photoBytes;
        private final StreamObserver<SubmitPhotoResponse> responseObserver;

        public PhotoRequestStreamObserver(StreamObserver<SubmitPhotoResponse> responseObserver) {
            this.photoBytes = new ByteArrayOutputStream();
            this.responseObserver = responseObserver;
        }

        @Override
        public void onNext(SubmitPhotoRequest submitPhotoRequest) {
            try {
                photoBytes.write(submitPhotoRequest.getPhoto().toByteArray());
                photoName = submitPhotoRequest.getPhotoName();
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
                    "Finished receiving photo bytes: Assigned new request %s", requestId
            ));

            responseObserver.onNext(SubmitPhotoResponse.newBuilder()
                    .setRequestId(requestId)
                    .build());
            responseObserver.onCompleted();

            LandmarksServerLogger.logger.info(String.format("Submitting photo for request %s", requestId));
            try {
                service.submitPhoto(requestId, photoBytes.toByteArray(), photoName);

                LandmarksServerLogger.logger.info(String.format(
                        "Successfully submitted photo with request %s", requestId
                ));
            } catch (PhotoSubmissionException e) {
                LandmarksServerLogger.logger.info(String.format(
                        "Error during photo submission of request %s", requestId
                ));
            }
        }
    }
}