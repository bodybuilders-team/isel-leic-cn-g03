package pt.isel.cn.landmarks.storage.metadata;

import pt.isel.cn.landmarks.domain.Landmark;

import java.util.List;

/**
 * Interface for storing metadata about the requests and landmarks.
 */
public interface MetadataStorage {

    /**
     * Stores the metadata of a request.
     *
     * @param requestId The id of the request.
     * @param imageUrl  The url of the image to be processed.
     */
    void storeRequestMetadata(String requestId, String timestamp, String imageUrl);

    /**
     * Stores the metadata of the landmarks.
     *
     * @param requestId The id of the request that generated the landmark.
     * @param landmarks The landmarks to be stored.
     */
    void storeLandmarksMetadata(String requestId, List<Landmark> landmarks);
}
