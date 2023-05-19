package pt.isel.cn.landmarks.app;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarksService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.metadatastorage.MetadataService;
import pt.isel.cn.landmarks.app.services.pubsub.MessageReceiveHandler;
import pt.isel.cn.landmarks.app.services.pubsub.PubsubService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Worker for the Landmarks Detector application.
 */
public class LandmarksWorker implements Runnable {

    // TODO: review the names of the classes and interfaces
    private final MetadataService metadataService;
    private final DataStorageService dataStorageService;
    private final LandmarksService landmarksService;
    private final MapsService mapsService;
    private final PubsubService pubsubService;

    /**
     * Constructor for the worker.
     *
     * @param metadataService    the metadata storage service
     * @param dataStorageService the data storage service
     * @param landmarksService   the landmarks service
     * @param mapsService        the map service
     * @param pubsubService      the Pub/Sub service
     */
    public LandmarksWorker(
            MetadataService metadataService,
            DataStorageService dataStorageService,
            LandmarksService landmarksService,
            MapsService mapsService,
            PubsubService pubsubService
    ) {
        this.metadataService = metadataService;
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

                    try {
                        String imageUrl = dataStorageService.getImageLocation(bucketName, blobName);

                        // Store the metadata in the Firestore
                        metadataService.storeRequestMetadata(requestId, timestamp, imageUrl);

                        // Process the image
                        LandmarksLogger.logger.info("Processing image: " + imageUrl);
                        List<Landmark> landmarks = landmarksService.detectLandmarks(imageUrl);
                        LandmarksLogger.logger.info("Found " + landmarks.size() + " landmarks");

                        List<LandmarkMetadata> landmarkMetadataList = landmarks.stream().map(landmark -> {
                            // Get the map for the landmark
                            byte[] map = mapsService.getStaticMap(landmark.getLocation());
                            landmark.setMap(map);

                            // Store the map in the Cloud Storage
                            String mapBlobName = dataStorageService.storeLandmarkMap(landmark);

                            return new LandmarkMetadata(
                                    landmark.getName(),
                                    landmark.getLocation(),
                                    landmark.getConfidence(),
                                    mapBlobName
                            );
                        }).collect(Collectors.toList());

                        // Store the landmarks in the Firestore
                        metadataService.storeLandmarksMetadata(requestId, landmarkMetadataList);

                        LandmarksLogger.logger.info("Finished processing request: " + requestId);
                    } catch (IOException e) {
                        LandmarksLogger.logger.info("I/O Error during processing of request: " + requestId);
                        e.printStackTrace();
                    }
                }
        ));
    }
}
