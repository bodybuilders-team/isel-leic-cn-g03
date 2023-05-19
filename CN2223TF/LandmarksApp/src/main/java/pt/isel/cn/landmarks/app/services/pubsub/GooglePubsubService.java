package pt.isel.cn.landmarks.app.services.pubsub;

import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

/**
 * Implementation of the {@link PubsubService} interface that uses Google Cloud Pub/Sub.
 */
public class GooglePubsubService implements PubsubService {

    @Override
    public void subscribe(String projectId, String subscriptionId, MessageReceiveHandler handler) {
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
