package pt.isel.cn.landmarks.app.services.datastorage;

import com.google.cloud.Timestamp;
import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.domain.RequestMetadata;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.app.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static pt.isel.cn.landmarks.app.Config.MAPS_BUCKET_NAME;

/**
 * Service for the storage operations of the app.
 */
public class StorageService {

    private final CloudDataStorage cloudDataStorage;
    private final MetadataStorage metadataStorage;

    public StorageService(CloudDataStorage cloudDataStorage, MetadataStorage metadataStorage) {
        this.cloudDataStorage = cloudDataStorage;
        this.metadataStorage = metadataStorage;
    }

    /**
     * Gets the photo url for the provided bucket and blob names.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the photo url
     */
    public String getPhotoUrl(String bucketName, String blobName) {
        return cloudDataStorage.getBlobLocation(bucketName, blobName);
    }

    /**
     * Gets the photo for the provided bucket and blob names in byte array form.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the photo in byte array form
     */
    public byte[] getPhoto(String bucketName, String blobName) {
        try {
            return cloudDataStorage.downloadBlobFromBucket(bucketName, blobName);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Stores the landmark map in the cloud storage.
     *
     * @param landmark the landmark to store the map of
     * @return the map blob name or null if an error occurred
     */
    public String storeLandmarkMap(Landmark landmark) {
        String blobName = UUID.randomUUID().toString();

        try {
            cloudDataStorage.uploadBlobToBucket(MAPS_BUCKET_NAME, blobName, landmark.getMap(), null);
        } catch (IOException e) {
            return null;
        }

        cloudDataStorage.makeBlobPublic(MAPS_BUCKET_NAME, blobName);

        return blobName;
    }

    /**
     * Stores the metadata of a request.
     *
     * @param requestId the id of the request
     * @param photoName the name of the photo
     * @param timestamp the timestamp of the request
     * @param photoUrl  the url of the photo
     */
    public void storeRequestMetadata(String requestId, String photoName, String timestamp, String photoUrl) {
        RequestMetadata requestMetadata = new RequestMetadata(
                requestId,
                photoName,
                Timestamp.parseTimestamp(timestamp),
                photoUrl,
                "PENDING",
                null
        );

        metadataStorage.storeRequestMetadata(requestMetadata);
    }

    /**
     * Stores the landmarks in the request metadata and updates the status to "DONE".
     *
     * @param requestId the id of the request
     * @param landmarks the landmarks to be stored
     */
    public void storeLandmarksMetadata(String requestId, List<LandmarkMetadata> landmarks) {
        metadataStorage.updateLandmarksAndStatus(requestId, landmarks, "DONE");
    }
}
