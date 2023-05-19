package pt.isel.cn.landmarks.server.service.maps;

import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;

import java.io.IOException;

import static pt.isel.cn.landmarks.server.Config.MAPS_BUCKET_NAME;

/**
 * Service for handling operations related to the map image retrieval.
 */
public class MapService {

    CloudDataStorage cloudDataStorage;

    public MapService(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    /**
     * Retrieves the map image using the provided blob name.
     *
     * @param mapBlobName the name of the blob of the map image
     * @return the map image in byte array form or null if the map image could not be retrieved
     */
    public byte[] getMapImage(String mapBlobName) {
        try {
            return cloudDataStorage.downloadBlobFromBucket(MAPS_BUCKET_NAME, mapBlobName);
        } catch (IOException e) {
            return null;
        }
    }
}
