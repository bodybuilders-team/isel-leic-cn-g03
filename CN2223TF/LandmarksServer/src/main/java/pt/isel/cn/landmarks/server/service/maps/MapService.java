package pt.isel.cn.landmarks.server.service.maps;

import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;

import java.io.IOException;

public class MapService {

    private static final String MAPS_BUCKET_NAME = "landmarks-maps";

    CloudDataStorage cloudDataStorage;

    public MapService(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    public byte[] getMapImage(String mapBlobName) {
        try {
            return cloudDataStorage.downloadBlobFromBucket(MAPS_BUCKET_NAME, mapBlobName);
        }
        catch (IOException e) {
            return null;
        }
    }
}
