package pt.isel.cn.landmarks.app.services.datastorage;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;

import java.io.IOException;

import static pt.isel.cn.landmarks.app.Config.MAPS_BUCKET_NAME;

/**
 * Service that handles storage of image and map data.
 */
public class DataStorageService {

    private final CloudDataStorage cloudDataStorage;

    public DataStorageService(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    /**
     * Gets the image location for the provided bucket and blob names.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the image location
     */
    public String getImageLocation(String bucketName, String blobName) {
        return cloudDataStorage.getBlobLocation(bucketName, blobName);
    }

    /**
     * Gets the image for the provided bucket and blob names in byte array form.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the image in byte array form
     */
    public byte[] getImage(String bucketName, String blobName) {
        try {
            return cloudDataStorage.downloadBlobFromBucket(bucketName, blobName);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Stores the landmark map.
     *
     * @param landmark the landmark to store the map of
     * @return the map blob name or null if an error occurred
     */
    public String storeLandmarkMap(Landmark landmark) {
        try {
            cloudDataStorage.uploadBlobToBucket(MAPS_BUCKET_NAME, landmark.getName(), landmark.getMap(), "image/png");
        } catch (IOException e) {
            return null;
        }

        cloudDataStorage.makeBlobPublic(MAPS_BUCKET_NAME, landmark.getName());

        return landmark.getName();
    }
}
