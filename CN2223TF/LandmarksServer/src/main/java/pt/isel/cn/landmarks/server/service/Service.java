package pt.isel.cn.landmarks.server.service;

import pt.isel.cn.landmarks.server.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.server.domain.RequestMetadata;
import pt.isel.cn.landmarks.server.service.dtos.GetResultsOutput;
import pt.isel.cn.landmarks.server.service.dtos.IdentifiedPhotoOutput;
import pt.isel.cn.landmarks.server.service.exceptions.LandmarkDetectionException;
import pt.isel.cn.landmarks.server.service.exceptions.ImageSubmissionException;
import pt.isel.cn.landmarks.server.service.exceptions.InvalidConfidenceThresholdException;
import pt.isel.cn.landmarks.server.service.exceptions.MapImageRetrievalException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotFoundException;
import pt.isel.cn.landmarks.server.service.exceptions.RequestNotProcessedException;
import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.server.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pt.isel.cn.landmarks.server.Config.IMAGES_BUCKET_NAME;
import static pt.isel.cn.landmarks.server.Config.MAPS_BUCKET_NAME;

/**
 * Service for handling operations of the server.
 */
public class Service {

    private final CloudDataStorage cloudDataStorage;
    private final MetadataStorage metadataStorage;
    private final LandmarksDetector landmarksDetector;

    public Service(CloudDataStorage cloudDataStorage, MetadataStorage metadataStorage, LandmarksDetector landmarksDetector) {
        this.cloudDataStorage = cloudDataStorage;
        this.metadataStorage = metadataStorage;
        this.landmarksDetector = landmarksDetector;
    }

    /**
     * Submits an image for processing.
     *
     * @param requestId  the id of the request
     * @param imageBytes the image in byte array form
     * @param photoName  the name of the photo
     * @throws ImageSubmissionException if there was an error submitting the image
     */
    public void submitImage(String requestId, byte[] imageBytes, String photoName) throws ImageSubmissionException {
        String blobName = UUID.randomUUID().toString();

        try {
            cloudDataStorage.uploadBlobToBucket(IMAGES_BUCKET_NAME, blobName, imageBytes, null);
            cloudDataStorage.makeBlobPublic(IMAGES_BUCKET_NAME, blobName);

            landmarksDetector.notifyAboutRequest(requestId, photoName, blobName);
        } catch (IOException | LandmarkDetectionException e) {
            throw new ImageSubmissionException(String.format("Error submitting image %s", photoName));
        }
    }

    /**
     * Gets the results of the request with the provided id.
     *
     * @param requestId the id of the request
     * @return the results of the request
     * @throws RequestNotFoundException     if the request with the provided id was not found
     * @throws RequestNotProcessedException if the request with the provided id didn't finish processing yet
     * @throws MapImageRetrievalException   if the map image could not be retrieved
     */
    public GetResultsOutput getResults(String requestId) throws RequestNotFoundException, RequestNotProcessedException, MapImageRetrievalException {
        Optional<RequestMetadata> requestOptional = metadataStorage.getRequestMetadata(requestId);

        if (requestOptional.isEmpty()) {
            throw new RequestNotFoundException(String.format("Request with id %s not found", requestId));
        }

        RequestMetadata requestMetadata = requestOptional.get();
        if (!requestMetadata.getStatus().equals("DONE")) {
            throw new RequestNotProcessedException(String.format("Request with id %s didn't finish processing yet", requestId));
        }

        List<LandmarkMetadata> landmarkMetadataList = requestMetadata.getLandmarks();

        if (landmarkMetadataList.isEmpty()) {
            return new GetResultsOutput(landmarkMetadataList, null);
        }

        LandmarkMetadata highestConfidenceLandmark = landmarkMetadataList.stream()
                .max(Comparator.comparingDouble(LandmarkMetadata::getConfidence))
                .get();

        byte[] mapImage = getMapImage(highestConfidenceLandmark.getMapBlobName());
        if (mapImage == null) { // TODO if one fails retrieve another?
            throw new MapImageRetrievalException(String.format(
                    "Error retrieving landmark map from request with id %s", requestId
            ));
        }

        return new GetResultsOutput(requestMetadata.getLandmarks(), mapImage);
    }

    /**
     * Gets all identified photos containing at least a landmark within the provided confidence threshold.
     *
     * @param confidenceThreshold the confidence threshold
     * @return the list of identified photos
     * @throws InvalidConfidenceThresholdException if the confidence threshold is invalid
     */
    public List<IdentifiedPhotoOutput> getIdentifiedPhotos(float confidenceThreshold) throws InvalidConfidenceThresholdException {
        if (confidenceThreshold < 0 || confidenceThreshold > 1) {
            throw new InvalidConfidenceThresholdException(String.format(
                    "Confidence threshold of %f is invalid. Must be between 0 and 1", confidenceThreshold
            ));
        }

        return metadataStorage.getAllRequestMetadata()
                .stream()
                .filter(requestMetadata -> requestMetadata.getLandmarks().stream()
                        .anyMatch(landmarkMetadata -> landmarkMetadata.getConfidence() >= confidenceThreshold))
                .map(requestMetadata -> {
                            @SuppressWarnings("OptionalGetWithoutIsPresent")
                            LandmarkMetadata highestConfidenceLandmark = requestMetadata.getLandmarks().stream()
                                    .filter(landmarkMetadata -> landmarkMetadata.getConfidence() >= confidenceThreshold)
                                    .max(Comparator.comparingDouble(LandmarkMetadata::getConfidence))
                                    .get();

                            return new IdentifiedPhotoOutput(
                                    requestMetadata.getPhotoName(),
                                    highestConfidenceLandmark.getName(),
                                    highestConfidenceLandmark.getConfidence()
                            );
                        }
                ).collect(Collectors.toList());
    }

    /**
     * Retrieves the map image using the provided blob name.
     *
     * @param mapBlobName the name of the blob of the map image
     * @return the map image in byte array form or null if the map image could not be retrieved
     */
    private byte[] getMapImage(String mapBlobName) {
        try {
            return cloudDataStorage.downloadBlobFromBucket(MAPS_BUCKET_NAME, mapBlobName);
        } catch (IOException e) {
            return null;
        }
    }
}
