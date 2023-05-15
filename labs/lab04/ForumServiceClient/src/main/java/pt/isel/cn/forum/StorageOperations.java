package pt.isel.cn.forum;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Operations on Google Storage.
 */
public class StorageOperations {

    Storage storage;

    public StorageOperations(Storage storage) {
        this.storage = storage;
    }

    /**
     * Gets the storage class.
     *
     * @return the storage class
     */
    StorageClass getStorageClass() {
        Scanner scan = new Scanner(System.in);
        StorageClass[] classes = new StorageClass[]{
                StorageClass.STANDARD,
                StorageClass.NEARLINE,
                StorageClass.COLDLINE,
                StorageClass.ARCHIVE
        };

        int option;
        do {
            System.out.println("Options for Google Storage Class:");
            System.out.println("1: STANDARD");
            System.out.println("2: NEARLINE");
            System.out.println("3: COLDLINE");
            System.out.println("4: ARCHIVE");
            System.out.print("Choose an option: ");
            option = scan.nextInt();
        } while (!(option > 0 && option <= 4));
        return classes[option - 1];
    }

    /**
     * Gets the storage location.
     *
     * @return the storage location
     */
    String getLocation() {
        Scanner scan = new Scanner(System.in);
        String[] locals = new String[]{
                "northamerica-northeast1", "us-central1", "us-east1", "us-east4", "us-west1", "us-west2",
                "southamerica-east1",
                "europe-north1", "europe-west1", "europe-west2", "europe-west3", "europe-west4", "europe-west6",
                "asia-east1", "asia-east2", "asia-northeast1", "asia-south1", "asia-southeast1",
                "australia-southeast1", "asia", "eu", "us", "EUR4", "NAM4"
        };

        int option;
        do {
            System.out.println("Region names:");
            System.out.println("North America:");
            System.out.println("1:northamerica-northeast1	 (Montréal)");
            System.out.println("2:us-central1	(Iowa)");
            System.out.println("3:us-east1	(South Carolina)");
            System.out.println("4: us-east4	(Northern Virginia)");
            System.out.println("5:us-west1	(Oregon)");
            System.out.println("6:us-west2	(Los Angeles)");
            System.out.println("South America:");
            System.out.println("7:southamerica-east1	(São Paulo)");
            System.out.println("Europe");
            System.out.println("8:europe-north1	(Finland)");
            System.out.println("9:europe-west1	(Belgium)");
            System.out.println("10:europe-west2	(London)");
            System.out.println("11:europe-west3	(Frankfurt)");
            System.out.println("12:europe-west4	(Netherlands)");
            System.out.println("13:europe-west6	(Zürich)");
            System.out.println("Asia:");
            System.out.println("14:asia-east1	(Taiwan)");
            System.out.println("15:asia-east2	(Hong Kong)");
            System.out.println("16:asia-northeast1	(Tokyo)");
            System.out.println("17:asia-south1	(Mumbai)");
            System.out.println("18:asia-southeast1	(Singapore)");
            System.out.println("Australia:");
            System.out.println("19:australia-southeast1	(Sydney)");
            System.out.println("Multi-regional locations:");
            System.out.println("20:asia	(Data centers in Asia)");
            System.out.println("21:eu	 (Data centers in the European Union1)");
            System.out.println("22:us	 (Data centers in the United States)");
            System.out.println("Dual-region locations:");
            System.out.println("23: EUR4 	EUROPE-NORTH1 and EUROPE-WEST4");
            System.out.println("24: NAM4 	US-CENTRAL1 and US-EAST1");
            System.out.print("Choose an option: ");
            option = scan.nextInt();
        } while (!(option > 0 && option <= 24));
        return locals[option - 1];
    }

    /**
     * Creates a bucket.
     *
     * @param bucketName the name of the bucket to create
     * @return the created bucket
     */
    public Bucket createBucket(String bucketName) {
        return storage.create(
                BucketInfo.newBuilder(bucketName)
                        // See here for possible values: http://g.co/cloud/storage/docs/storage-classes
                        .setStorageClass(getStorageClass())
                        // Possible values: http://g.co/cloud/storage/docs/bucket-locations#location-mr
                        .setLocation(getLocation())
                        .build()
        );
    }

    /**
     * Deletes a bucket.
     *
     * @param bucketName the name of the bucket to delete
     */
    public void deleteBucket(String bucketName) {
        Bucket bucket = storage.get(bucketName);
        bucket.delete();
    }

    /**
     * Uploads a blob to the bucket.
     *
     * @param bucketName  the name of the bucket
     * @param blobName    the name of the blob
     * @param absFileName the path to the file to upload
     * @throws IOException on IO error
     */
    public void uploadBlobToBucket(String bucketName, String blobName, String absFileName) throws IOException {
        Path uploadFrom = Paths.get(absFileName);
        String contentType = Files.probeContentType(uploadFrom);
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        if (Files.size(uploadFrom) > 1_000_000) {
            // When content is not available or large (1MB or more) it is recommended
            // to write it in chunks via the blob's channel writer.
            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = Files.newInputStream(uploadFrom)) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        try {
                            writer.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else {
            byte[] bytes = Files.readAllBytes(uploadFrom);
            storage.create(blobInfo, bytes);
        }
    }

    /**
     * Downloads a blob from the bucket.
     *
     * @param bucketName  the name of the bucket
     * @param blobName    the name of the blob
     * @param absFileName the path to download the file to
     * @throws IOException on IO error
     */
    public void downloadBlobFromBucket(String bucketName, String blobName, String absFileName) throws IOException {
        Path downloadTo = Paths.get(absFileName);
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalArgumentException("No such Blob exists !");

        if (!Files.exists(downloadTo.getParent()))
            Files.createDirectories(downloadTo.getParent());

        // Check if the file exists and create it if not
        if (!Files.exists(downloadTo))
            Files.createFile(downloadTo);

        PrintStream writeTo = new PrintStream(Files.newOutputStream(downloadTo));
        if (blob.getSize() < 1_000_000) {
            // Blob is small read all its content in one request
            byte[] content = blob.getContent();
            writeTo.write(content);
        } else {
            // When Blob size is big or unknown use the blob's channel reader.
            try (ReadChannel reader = blob.reader()) {
                WritableByteChannel channel = Channels.newChannel(writeTo);
                ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                while (reader.read(bytes) > 0) {
                    bytes.flip();
                    channel.write(bytes);
                    bytes.clear();
                }
            }
        }
        writeTo.close();
    }

    /**
     * Makes a blob public.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     */
    public void makeBlobPublic(String bucketName, String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null)
            throw new IllegalArgumentException("No such Blob exists !");

        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        Acl.Role role = Acl.Role.READER;

        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
    }

    /**
     * Checks if a blob exists in a bucket.
     *
     * @param bucketName the name of the bucket
     * @param blobName   the name of the blob
     * @return true if the blob exists, false otherwise
     */
    public boolean checkBlobExists(String bucketName, String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        return blob != null;
    }
}
