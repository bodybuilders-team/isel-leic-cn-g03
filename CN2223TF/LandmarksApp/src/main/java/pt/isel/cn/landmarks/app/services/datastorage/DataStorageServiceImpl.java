package pt.isel.cn.landmarks.app.services.datastorage;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;

import java.io.IOException;

public class DataStorageServiceImpl implements DataStorageService {

    private static final String MAPS_BUCKET_NAME = "landmarks-maps";

    private final CloudDataStorage cloudDataStorage;

    public DataStorageServiceImpl(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    @Override
    public String getImageLocation(String bucketName, String blobName) {
        return cloudDataStorage.getBlobLocation(bucketName, blobName);
    }

    @Override
    public void storeLandmarkMap(Landmark landmark) {
        try {
            cloudDataStorage.uploadBlobToBucket(MAPS_BUCKET_NAME, landmark.getName(), landmark.getMap(), "image/png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cloudDataStorage.makeBlobPublic(MAPS_BUCKET_NAME, landmark.getName());
    }
}
