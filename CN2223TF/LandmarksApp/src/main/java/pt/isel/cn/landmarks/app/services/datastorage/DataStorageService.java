package pt.isel.cn.landmarks.app.services.datastorage;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;

import java.io.IOException;

/**
 * The data storage service that handles image and map data.
 */
public class DataStorageService {

    private static final String MAPS_BUCKET_NAME = "landmarks-maps";

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
     * Stores the landmark map.
     *
     * @param landmark the landmark whose map to store
     * @return the map blob name
     */
    public String storeLandmarkMap(Landmark landmark) {
        try {
            cloudDataStorage.uploadBlobToBucket(MAPS_BUCKET_NAME, landmark.getName(), landmark.getMap(), "image/png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cloudDataStorage.makeBlobPublic(MAPS_BUCKET_NAME, landmark.getName());

        return landmark.getName();
    }
}
