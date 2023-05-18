package pt.isel.cn.landmarks.storage.data;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class CloudDataStorage implements DataStorage {

    Storage storage;

    @Override
    public void uploadBlobToBucket(String bucketName, String blobName, byte[] blobBytes) throws IOException {
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build(); // TODO .setContentType(contentType) needed?

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

        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalStateException("No such Blob exists !");

        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        Acl.Role role = Acl.Role.READER;

        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
    }

    @Override
    public byte[] downloadBlobFromBucket(String bucketName, String blobName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalArgumentException("No such Blob exists !");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (blob.getSize() < 1_000_000) {
            // Blob is small read all its content in one request
            byte[] content = blob.getContent();
            outputStream.write(content);
        } else {
            // When Blob size is big or unknown use the blob's channel reader.
            try (ReadChannel reader = blob.reader()) {
                WritableByteChannel channel = Channels.newChannel(outputStream);
                ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                while (reader.read(bytes) > 0) {
                    bytes.flip();
                    channel.write(bytes);
                    bytes.clear();
                }
            }
        }

        byte[] blobBytes = outputStream.toByteArray();
        outputStream.close();
        return blobBytes;
    }
}
