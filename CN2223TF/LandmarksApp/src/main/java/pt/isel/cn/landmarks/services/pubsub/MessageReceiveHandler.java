package pt.isel.cn.landmarks.services.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

/**
 * Receives Pub/Sub messages and processes them using the processing function.
 */
public class MessageReceiveHandler implements MessageReceiver {

    private final MessageProcessingFunction processingFunction;

    public MessageReceiveHandler(MessageProcessingFunction processingFunction) {
        this.processingFunction = processingFunction;
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer replyConsumer) {
        String requestId = message.getAttributesOrThrow("requestId");
        String timestamp = message.getAttributesOrThrow("timestamp");
        String bucketName = message.getAttributesOrThrow("bucket");
        String blobName = message.getAttributesOrThrow("blob");

        processingFunction.processRequest(requestId, timestamp, bucketName, blobName);

        replyConsumer.ack();
    }
}
