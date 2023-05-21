package pt.isel.cn.landmarks.server.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import pt.isel.cn.landmarks.server.service.exceptions.LandmarkDetectionException;

import java.time.LocalDateTime;

import static pt.isel.cn.landmarks.server.Config.IMAGES_BUCKET_NAME;
import static pt.isel.cn.landmarks.server.Config.PROJECT_ID;
import static pt.isel.cn.landmarks.server.Config.TOPIC_ID;

/**
 * Implementation of {@link LandmarksDetector} that uses Google Pub/Sub to notify an external landmarks detector about new requests.
 */
public class PubSubLandmarksDetector implements LandmarksDetector {
    @Override
    public void notifyAboutRequest(String requestId, String photoName, String imageLocation) throws LandmarkDetectionException {
        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_ID);
        try {
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
        catch (Exception e) {
            throw new LandmarkDetectionException("Failed to notify landmarks detector about request");
        }
    }
}
