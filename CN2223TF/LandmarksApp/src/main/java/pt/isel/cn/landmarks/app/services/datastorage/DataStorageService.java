package pt.isel.cn.landmarks.app.services.datastorage;

import pt.isel.cn.landmarks.app.domain.Landmark;

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
     * @param landmark The landmark whose map to store.
     */
    void storeLandmarkMap(Landmark landmark);
}
