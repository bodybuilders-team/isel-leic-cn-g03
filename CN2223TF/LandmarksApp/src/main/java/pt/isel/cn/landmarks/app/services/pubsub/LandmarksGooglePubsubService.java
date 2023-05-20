package pt.isel.cn.landmarks.app.services.pubsub;

import static pt.isel.cn.landmarks.app.Config.PROJECT_ID;
import static pt.isel.cn.landmarks.app.Config.SUBSCRIPTION_ID;

/**
 * Service that allows subscribing to the Landmarks Pub/Sub topic via Google Cloud's Pub/Sub.
 */
public class LandmarksGooglePubsubService {

    private final GooglePubsubService googlePubsubService;

    public LandmarksGooglePubsubService(GooglePubsubService googlePubsubService) {
        this.googlePubsubService = googlePubsubService;
    }

    /**
     * Subscribes to the Landmarks Pub/Sub topic and processes the received messages using the provided processing function.
     *
     * @param processingFunction the processing function to process the received messages
     */
    public void subscribe(LandmarksMessageProcessingFunction processingFunction) {
        googlePubsubService.subscribe(PROJECT_ID, SUBSCRIPTION_ID, new LandmarksMessageReceiveHandler(processingFunction));
    }
}
