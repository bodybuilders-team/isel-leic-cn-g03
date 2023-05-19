package pt.isel.cn.landmarks.app.services.pubsub;

/**
 * Service that allows subscribing to Pub/Sub topics.
 */
public interface PubsubService {

    /**
     * Subscribes to the Pub/Sub topic with the provided projectId and subscriptionId, and
     * processes the received messages using the provided handler.
     *
     * @param projectId      The project id.
     * @param subscriptionId The subscription id.
     * @param handler        The handler to process the received messages.
     */
    void subscribe(String projectId, String subscriptionId, MessageReceiveHandler handler);
}
