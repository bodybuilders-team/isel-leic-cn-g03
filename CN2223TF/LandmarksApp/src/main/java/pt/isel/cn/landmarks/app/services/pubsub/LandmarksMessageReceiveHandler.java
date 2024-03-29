package pt.isel.cn.landmarks.app.services.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

/**
 * Receives Landmarks Pub/Sub messages and processes them using the processing function.
 */
public class LandmarksMessageReceiveHandler implements MessageReceiver {

    private final LandmarksMessageProcessingFunction processingFunction;

    public LandmarksMessageReceiveHandler(LandmarksMessageProcessingFunction processingFunction) {
        this.processingFunction = processingFunction;
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer replyConsumer) {
        String requestId = message.getAttributesOrThrow("requestId");
        String photoName = message.getAttributesOrThrow("photoName");
        String timestamp = message.getAttributesOrThrow("timestamp");
        String bucketName = message.getAttributesOrThrow("bucket");
        String blobName = message.getAttributesOrThrow("blob");

        processingFunction.processRequest(requestId, photoName, timestamp, bucketName, blobName);

        replyConsumer.ack();
    }
}
