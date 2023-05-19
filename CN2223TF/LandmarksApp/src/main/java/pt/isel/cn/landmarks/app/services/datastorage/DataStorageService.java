package pt.isel.cn.landmarks.app.services.datastorage;

import pt.isel.cn.landmarks.app.domain.Landmark;

/**
 * The interface for the data storage service that handles image and map data.
 */
public interface DataStorageService {

    /**
     * Gets the image location for the provided bucket and blob names.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the image location
     */
    String getImageLocation(String bucketName, String blobName);

    /**
     * Stores the landmark map.
     *
     * @param landmark the landmark whose map to store
     * @return the map blob name
     */
    String storeLandmarkMap(Landmark landmark);
}
