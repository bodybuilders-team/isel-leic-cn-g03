package pt.isel.cn.landmarks.server.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ImageServiceImpl implements ImageService {

    private static final String PROJECT_ID = "cn2223-t1-g03";
    private static final String TOPIC_ID = "landmarks";
    private static final String IMAGES_BUCKET_NAME = "landmarks-images";
    CloudDataStorage cloudDataStorage;

    public ImageServiceImpl(CloudDataStorage cloudDataStorage) {
        this.cloudDataStorage = cloudDataStorage;
    }

    @Override
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

    @Override
    public void notifyImageUploaded(String requestId, String imageLocation) throws IOException, ExecutionException, InterruptedException {
        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_ID);
        Publisher publisher = Publisher.newBuilder(tName).build();
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .putAttributes("requestId", requestId)
                .putAttributes("timestamp", LocalDateTime.now().toString())
                .putAttributes("bucket", IMAGES_BUCKET_NAME)
                .putAttributes("blob", imageLocation)
                .build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        future.get();
        publisher.shutdown();
    }
}
