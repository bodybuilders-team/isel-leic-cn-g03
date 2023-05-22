package pt.isel.cn.landmarks.app.services.pubsub;

import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

/**
 * Service that allows subscribing to Google Cloud's Pub/Sub topics.
 */
public class GooglePubsubService {

    /**
     * Subscribes to the Pub/Sub topic with the provided projectId and subscriptionId, and
     * processes the received messages using the provided handler.
     *
     * @param projectId      the project id
     * @param subscriptionId the subscription id
     * @param handler        the handler to process the received messages
     */
    public void subscribe(String projectId, String subscriptionId, MessageReceiver handler) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder()
                .setExecutorThreadCount(1)
                .build();

        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, handler)
                .setExecutorProvider(executorProvider)
                .build();

        subscriber.startAsync().awaitRunning();
    }
}
