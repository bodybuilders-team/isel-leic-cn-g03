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
     * Stores the metadata of the landmarks.
     *
     * @param requestId the id of the request that generated the landmark
     * @param landmarks the landmarks to be stored
     */
    void storeLandmarksMetadata(String requestId, List<LandmarkMetadata> landmarks);
}
