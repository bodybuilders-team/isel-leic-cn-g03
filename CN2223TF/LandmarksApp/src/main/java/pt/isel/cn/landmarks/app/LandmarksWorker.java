package pt.isel.cn.landmarks.app;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarksService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.pubsub.MessageReceiveHandler;
import pt.isel.cn.landmarks.app.services.pubsub.PubsubService;
import pt.isel.cn.landmarks.app.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.List;


/**
 * Worker for the Landmarks Detector application.
 */
public class LandmarksWorker implements Runnable {

    // TODO: review the names of the classes and interfaces
    private final MetadataStorage metadataStorage;
    private final DataStorageService dataStorageService;
    private final LandmarksService landmarksService;
    private final MapsService mapsService;
    private final PubsubService pubsubService;

    /**
     * Constructor for the worker.
     *
     * @param metadataStorage    The metadata storage service.
     * @param dataStorageService The data storage service.
     * @param landmarksService   The landmarks service.
     * @param mapsService        The map service.
     * @param pubsubService      The Pub/Sub service.
     */
    public LandmarksWorker(
            MetadataStorage metadataStorage,
            DataStorageService dataStorageService,
            LandmarksService landmarksService,
            MapsService mapsService,
            PubsubService pubsubService
    ) {
        this.metadataStorage = metadataStorage;
        this.dataStorageService = dataStorageService;
        this.landmarksService = landmarksService;
        this.mapsService = mapsService;
        this.pubsubService = pubsubService;
    }


    /**
     * Runs the worker.
     */
    @Override
    public void run() {
        pubsubService.subscribe(Main.PROJECT_ID, Main.SUBSCRIPTION_ID, new MessageReceiveHandler(
                (String requestId, String timestamp, String bucketName, String blobName) -> {
                    LandmarksLogger.logger.info("Received request: " + requestId);

                    String imageUrl = dataStorageService.getImageLocation(bucketName, blobName);

                    try {
                        // Store the metadata in the Firestore
                        metadataStorage.storeRequestMetadata(requestId, timestamp, imageUrl);

                        // Process the image
                        LandmarksLogger.logger.info("Processing image: " + imageUrl);
                        List<Landmark> landmarks = landmarksService.detectLandmarks(imageUrl);
                        LandmarksLogger.logger.info("Found " + landmarks.size() + " landmarks");

                        landmarks.forEach(landmark -> {
                            // Get the map for the landmark
                            byte[] map = mapsService.getStaticMap(landmark.getLocation());
                            landmark.setMap(map);

                            // Store the map in the Cloud Storage
                            dataStorageService.storeLandmarkMap(landmark);
                        });

                        // Store the metadata in the Firestore
                        metadataStorage.storeLandmarksMetadata(requestId, landmarks);

                        LandmarksLogger.logger.info("Finished processing request: " + requestId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));
    }
}
