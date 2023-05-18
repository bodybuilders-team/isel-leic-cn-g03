package pt.isel.cn.landmarks;

import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.storage.StorageOptions;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import pt.isel.cn.landmarks.domain.Landmark;
import pt.isel.cn.landmarks.service.landmarks.LandmarksService;
import pt.isel.cn.landmarks.service.landmarks.LandmarksServiceVision;
import pt.isel.cn.landmarks.service.map.MapService;
import pt.isel.cn.landmarks.service.map.MapServiceMapsStatic;
import pt.isel.cn.landmarks.storage.data.CloudStorageService;
import pt.isel.cn.landmarks.storage.data.DataStorage;
import pt.isel.cn.landmarks.storage.metadata.FirestoreService;
import pt.isel.cn.landmarks.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.List;


/**
 * Worker for the Landmarks Detector application.
 */
public class LandmarksApp {

    private static final String PROJECT_ID = "cn2223-t1-g03";
    private static final String SUBSCRIPTION_ID = "landmarks-sub";

    // TODO: review the names of the classes and interfaces
    private static DataStorage dataStorage;
    private static MetadataStorage metadataStorage;
    private static LandmarksService landmarksService;
    private static MapService mapService;

    /**
     * Entry point of the Landmarks application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) throws IOException {
        // Initialize the application
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        dataStorage = new CloudStorageService(StorageOptions.getDefaultInstance().getService());
        metadataStorage = new FirestoreService(FirestoreOptions.getDefaultInstance().getService()); // TODO: REMOVE COMMENT IF THIS WORK
        landmarksService = new LandmarksServiceVision();
        mapService = new MapServiceMapsStatic();

        // Start the Pub/Sub subscriber
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, SUBSCRIPTION_ID);
        ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder()
                .setExecutorThreadCount(1)
                .build();

        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiveHandler())
                .setExecutorProvider(executorProvider)
                .build();

        subscriber.startAsync().awaitRunning();
    }

    /**
     * Receives Pub/Sub messages and processes them.
     */
    private static class MessageReceiveHandler implements MessageReceiver {

        // Messages contain:
        // - requestId: the id of the request
        // - timestamp: the timestamp of the request
        // - bucket: the name of the bucket where the image is stored
        // - blob: the name of the image blob
        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer replyConsumer) {
            System.out.println("Received message: " + message.getData().toStringUtf8());

            String requestId = message.getAttributesOrThrow("requestId");
            String timestamp = message.getAttributesOrThrow("timestamp");
            String bucketName = message.getAttributesOrThrow("bucket");
            String blobName = message.getAttributesOrThrow("blob");
            String imageUrl = dataStorage.getImageLocation(bucketName, blobName);

            try {
                // Store the metadata in the Firestore
                metadataStorage.storeRequestMetadata(requestId, imageUrl);

                // Process the image
                List<Landmark> landmarks = landmarksService.detectLandmarks(imageUrl);

                landmarks.forEach(landmark -> {
                    // Get the map for the landmark
                    byte[] map = mapService.getStaticMap(landmark.getLocation());
                    landmark.setMap(map);

                    // Store the map in the Cloud Storage
                    dataStorage.storeLandmarkMap(landmark);

                    // Store the metadata in the Firestore
                    metadataStorage.storeLandmarkMetadata(requestId, landmark);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            replyConsumer.ack();
        }
    }
}