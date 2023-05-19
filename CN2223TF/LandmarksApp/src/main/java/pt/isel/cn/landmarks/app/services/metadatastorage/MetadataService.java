package pt.isel.cn.landmarks.app.services.metadatastorage;

import com.google.cloud.Timestamp;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.domain.RequestMetadata;
import pt.isel.cn.landmarks.app.storage.metadata.MetadataStorage;

import java.util.List;

/**
 * Service for handling metadata related to the requests and landmarks.
 */
public class MetadataService {

    MetadataStorage metadataStorage;

    public MetadataService(MetadataStorage metadataStorage) {
        this.metadataStorage = metadataStorage;
    }

    /**
     * Stores the metadata of a request.
     *
     * @param requestId the id of the request
     * @param timestamp the timestamp of the request
     * @param imageUrl  the url of the image
     */
    public void storeRequestMetadata(String requestId, String timestamp, String imageUrl) {
        RequestMetadata requestMetadata = new RequestMetadata(
                requestId,
                Timestamp.parseTimestamp(timestamp),
                imageUrl,
                "PENDING",
                null
        );

        metadataStorage.storeRequestMetadata(requestMetadata);
    }

    /**
     * Stores the landmarks in the request metadata and updates the status to "DONE".
     *
     * @param requestId the id of the request
     * @param landmarks the landmarks to be stored
     */
    public void storeLandmarksMetadata(String requestId, List<LandmarkMetadata> landmarks) {
        metadataStorage.updateLandmarksAndStatus(requestId, landmarks, "DONE");
    }
}
