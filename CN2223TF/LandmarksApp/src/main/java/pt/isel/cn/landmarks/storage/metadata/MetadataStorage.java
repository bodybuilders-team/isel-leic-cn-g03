package pt.isel.cn.landmarks.storage.metadata;

import pt.isel.cn.landmarks.domain.Landmark;

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
    void storeRequestMetadata(String requestId, String imageUrl);

    /**
     * Stores the metadata of a landmark.
     *
     * @param requestId The id of the request that generated the landmark.
     * @param landmark  The landmark to be stored.
     */
    void storeLandmarkMetadata(String requestId, Landmark landmark);
}
