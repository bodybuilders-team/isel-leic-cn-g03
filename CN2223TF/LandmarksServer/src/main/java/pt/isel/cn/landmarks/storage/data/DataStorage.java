package pt.isel.cn.landmarks.storage.data;

import java.io.IOException;

public interface DataStorage {

    /**
     * Uploads a blob to a bucket.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @param blob       the blob in byte array form
     * @throws IOException if an I/O error occurs
     */
    void uploadBlobToBucket(String bucketName, String blobName, byte[] blob) throws IOException;

    /**
     * Download a blob from a bucket.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the blob in byte array form
     * @throws IOException if an I/O error occurs
     */
    byte[] downloadBlobFromBucket(String bucketName, String blobName) throws IOException;
}
