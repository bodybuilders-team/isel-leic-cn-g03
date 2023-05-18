package pt.isel.cn.landmarks;

import pt.isel.cn.landmarks.domain.Landmark;
import pt.isel.cn.landmarks.services.landmarks.LandmarksService;
import pt.isel.cn.landmarks.services.maps.MapsService;
import pt.isel.cn.landmarks.services.pubsub.MessageReceiveHandler;
import pt.isel.cn.landmarks.services.pubsub.PubsubService;
import pt.isel.cn.landmarks.storage.data.DataStorage;
import pt.isel.cn.landmarks.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.List;

import static pt.isel.cn.landmarks.Main.PROJECT_ID;
import static pt.isel.cn.landmarks.Main.SUBSCRIPTION_ID;


/**
 * Worker for the Landmarks Detector application.
 */
public class LandmarksWorker implements Runnable {

    // TODO: review the names of the classes and interfaces
    private final DataStorage dataStorage;
    private final MetadataStorage metadataStorage;
    private final LandmarksService landmarksService;
    private final MapsService mapsService;
    private final PubsubService pubsubService;

    /**
     * Constructor for the worker.
     *
     * @param dataStorage      The data storage service.
     * @param metadataStorage  The metadata storage service.
     * @param landmarksService The landmarks service.
     * @param mapsService      The map service.
     * @param pubsubService    The Pub/Sub service.
     */
    public LandmarksWorker(
            DataStorage dataStorage,
            MetadataStorage metadataStorage,
            LandmarksService landmarksService,
            MapsService mapsService,
            PubsubService pubsubService
    ) {
        this.dataStorage = dataStorage;
        this.metadataStorage = metadataStorage;
        this.landmarksService = landmarksService;
        this.mapsService = mapsService;
        this.pubsubService = pubsubService;
    }


    /**
     * Runs the worker.
     */
    @Override
    public void run() {
        pubsubService.subscribe(PROJECT_ID, SUBSCRIPTION_ID, new MessageReceiveHandler(
                (String requestId, String timestamp, String bucketName, String blobName) -> {
                    String imageUrl = dataStorage.getImageLocation(bucketName, blobName);

                    try {
                        // Store the metadata in the Firestore
                        metadataStorage.storeRequestMetadata(requestId, timestamp, imageUrl);

                        // Process the image
                        List<Landmark> landmarks = landmarksService.detectLandmarks(imageUrl);

                        landmarks.forEach(landmark -> {
                            // Get the map for the landmark
                            byte[] map = mapsService.getStaticMap(landmark.getLocation());
                            landmark.setMap(map);

                            // Store the map in the Cloud Storage
                            dataStorage.storeLandmarkMap(landmark);

                            // Store the metadata in the Firestore
                            metadataStorage.storeLandmarkMetadata(requestId, landmark);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));
    }
}
