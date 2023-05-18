package pt.isel.cn.landmarks.storage.data;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import pt.isel.cn.landmarks.domain.Landmark;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Implementation of the {@link DataStorage} interface that uses Google Cloud Storage.
 */
public class CloudStorageService implements DataStorage {

    private final Storage storage;
    private static final String BUCKET_NAME = "landmarks-maps";

    public CloudStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String getImageLocation(String bucketName, String blobName) {
        return "gs://" + bucketName + "/" + blobName;
    }

    @Override
    public void storeLandmarkMap(Landmark landmark) {
        try {
            uploadBlobToBucket(landmark.getName(), landmark.getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }

        makeBlobPublic(landmark.getName());
    }

    /**
     * Uploads a blob to the bucket.
     *
     * @param blobName the name of the blob
     * @param blob     the blob's content
     * @throws IOException on IO error
     */
    private void uploadBlobToBucket(String blobName, byte[] blob) throws IOException {
        BlobId blobId = BlobId.of(CloudStorageService.BUCKET_NAME, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();

        if (blob.length > 1_000_000) {
            // When content is not available or large (1MB or more) it is recommended
            // to write it in chunks via the blob's channel writer.
            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try {
                    int limit = blob.length;
                    while (limit > 0) {
                        int chunk = Math.min(limit, 1024);
                        writer.write(ByteBuffer.wrap(buffer, 0, chunk));
                        limit -= chunk;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            storage.create(blobInfo, blob);
        }
    }

    /**
     * Makes a blob public.
     *
     * @param blobName the name of the blob
     */
    private void makeBlobPublic(String blobName) {
        BlobId blobId = BlobId.of(CloudStorageService.BUCKET_NAME, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalArgumentException("No such Blob exists!");

        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        Acl.Role role = Acl.Role.READER;

        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
    }
}
