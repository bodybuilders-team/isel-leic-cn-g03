package pt.isel.cn.landmarks.server.service.images;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static pt.isel.cn.landmarks.server.Config.IMAGES_BUCKET_NAME;
import static pt.isel.cn.landmarks.server.Config.PROJECT_ID;
import static pt.isel.cn.landmarks.server.Config.TOPIC_ID;

/**
 * Service for handling operations related to the image upload.
 */
public class ImageService {

    CloudDataStorage cloudDataStorage;

    public ImageService(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    /**
     * Uploads an image.
     *
     * @param imageBytes the image in byte array form
     * @return the location of the uploaded image
     */
    public String uploadImage(byte[] imageBytes) {
        String blobName = UUID.randomUUID().toString();

        try {
            cloudDataStorage.uploadBlobToBucket(IMAGES_BUCKET_NAME, blobName, imageBytes, "image/png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cloudDataStorage.makeBlobPublic(IMAGES_BUCKET_NAME, blobName);

        return blobName;
    }

    /**
     * Notifies the app that an image was uploaded.
     *
     * @param requestId     the id of the request
     * @param photoName     the name of the photo
     * @param imageLocation the location of the uploaded image
     */
    public void notifyImageUploaded(String requestId, String photoName, String imageLocation) throws IOException, ExecutionException, InterruptedException {
        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_ID);
        Publisher publisher = Publisher.newBuilder(tName).build();
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .putAttributes("requestId", requestId)
                .putAttributes("photoName", photoName)
                .putAttributes("timestamp", LocalDateTime.now().toString())
                .putAttributes("bucket", IMAGES_BUCKET_NAME)
                .putAttributes("blob", imageLocation)
                .build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        future.get();
        publisher.shutdown();
    }
}
