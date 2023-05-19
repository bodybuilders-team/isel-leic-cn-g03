package pt.isel.cn.landmarks.app.storage.data;

import java.io.IOException;

public interface CloudDataStorage {

    /**
     * Uploads a blob to a bucket.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @param blob       the blob in byte array form
     * @throws IOException if an I/O error occurs
     */
    void uploadBlobToBucket(String bucketName, String blobName, byte[] blob, String contentType) throws IOException;

    /**
     * Make blob public.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     */
    void makeBlobPublic(String bucketName, String blobName);

    /**
     * Gets the blob location for the provided bucket and blob names.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return the blob location
     */
    String getBlobLocation(String bucketName, String blobName);
}
