package pt.isel.cn.landmarks.app.storage.data;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Implementation of the {@link CloudDataStorage} interface that uses Google Cloud Storage.
 */
public class GoogleCloudCloudDataStorage implements CloudDataStorage {

    private final Storage storage;

    public GoogleCloudCloudDataStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void uploadBlobToBucket(String bucketName, String blobName, byte[] blobBytes, String contentType) throws IOException {
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        try (WriteChannel writer = storage.writer(blobInfo);
             ByteArrayInputStream blobBytesStream = new ByteArrayInputStream(blobBytes)) {
            byte[] buffer = new byte[1024];
            int limit;
            while ((limit = blobBytesStream.read(buffer)) >= 0) {
                try {
                    writer.write(ByteBuffer.wrap(buffer, 0, limit));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void makeBlobPublic(String bucketName, String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalStateException("No such Blob exists !");

        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        Acl.Role role = Acl.Role.READER;

        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
    }

    @Override
    public String getBlobLocation(String bucketName, String blobName) {
        return "gs://" + bucketName + "/" + blobName;
    }
}
