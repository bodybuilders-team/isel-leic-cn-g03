package pt.isel.cn.landmarks.app.storage.metadata;

import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.domain.RequestMetadata;

import java.util.List;

/**
 * Interface for handling metadata related to the requests and landmarks.
 */
public interface MetadataStorage {

    /**
     * Stores the metadata of a request.
     *
     * @param requestMetadata the request to be stored
     */
    void storeRequestMetadata(RequestMetadata requestMetadata);

    /**
     * Updates the request metadata with landmarks and status.
     *
     * @param requestId the id of the request
     * @param landmarks the landmarks to be stored
     * @param status    the status of the request
     */
    void updateLandmarksAndStatus(String requestId, List<LandmarkMetadata> landmarks, String status);
}
