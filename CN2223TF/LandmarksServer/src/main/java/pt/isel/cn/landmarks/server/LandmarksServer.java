package pt.isel.cn.landmarks.server;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import landmarks.GetResultsResponse;
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

        Optional<RequestMetadata> requestOptional = metadataStorage.getRequestMetadata(requestId);

        if (requestOptional.isEmpty()) {
            responseObserver.onError(new RuntimeException(String.format("Request with id %s not found", requestId)));
            return;
        }

        RequestMetadata requestMetadata = requestOptional.get();
        if (!requestMetadata.getStatus().equals("DONE")) {
            responseObserver.onError(new RuntimeException(String.format("Request with id %s didn't finish processing", requestId)));
            return;
        }

        List<LandmarkMetadata> landmarkMetadataList = List.of(requestMetadata.getLandmarks());

        byte[] mapImage = mapService.getMapImage(landmarkMetadataList.get(0).getMapBlobName());
        if (mapImage == null) { // TODO if one fails retrieve another?
            LandmarksServerLogger.logger.severe("Error retrieving map from landmark with name" + landmarkMetadataList.get(0).getMapBlobName());

            responseObserver.onError(new RuntimeException(
                    String.format("Error retrieving map from landmark with name %s", landmarkMetadataList.get(0).getMapBlobName()))
            );
            return;
        }

        responseObserver.onNext(GetResultsResponse.newBuilder()
                .setMap(ImageMap.newBuilder()
                        .setImageData(ByteString.copyFrom(mapImage))
                        .build())
                .addAllLandmarks(landmarkMetadataList.stream().map(landmarkMetadata ->
                        landmarks.Landmark.newBuilder()
                                .setName(landmarkMetadata.getName())
                                .setLatitude(landmarkMetadata.getLocation().getLatitude())
                                .setLongitude(landmarkMetadata.getLocation().getLongitude())
                                .setConfidence(landmarkMetadata.getConfidence())
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
                System.out.printf("Error during image upload of request with id %s", requestId);
            }
        }
    }
}