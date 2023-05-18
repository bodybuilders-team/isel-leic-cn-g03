package pt.isel.cn.landmarks.storage.data;

import pt.isel.cn.landmarks.domain.Landmark;

public interface DataStorage {

    /**
     * Gets the image location for the provided bucket and blob names.
     *
     * @param bucketName The name of the bucket.
     * @param blobName   The name of the blob.
     * @return The image location.
     */
    String getImageLocation(String bucketName, String blobName);

    /**
     * Stores the landmark map.
     *
     * @param landmark The landmark whose map to store.
     */
    void storeLandmarkMap(Landmark landmark);
}
