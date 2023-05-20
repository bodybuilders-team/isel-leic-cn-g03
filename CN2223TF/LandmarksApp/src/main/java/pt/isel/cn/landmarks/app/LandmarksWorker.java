package pt.isel.cn.landmarks.app;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.metadata.MetadataService;
import pt.isel.cn.landmarks.app.services.pubsub.LandmarksGooglePubsubService;

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
    private final LandmarkDetectionService landmarkDetectionService;
    private final MapsService mapsService;
    private final LandmarksGooglePubsubService pubsubService;

    /**
     * Constructor for the worker.
     *
     * @param metadataService          the metadata storage service
     * @param dataStorageService       the data storage service
     * @param landmarkDetectionService the landmarks service
     * @param mapsService              the map service
     * @param pubsubService            the Pub/Sub service
     */
    public LandmarksWorker(
            MetadataService metadataService,
            DataStorageService dataStorageService,
            LandmarkDetectionService landmarkDetectionService,
            MapsService mapsService,
            LandmarksGooglePubsubService pubsubService
    ) {
        this.metadataService = metadataService;
        this.dataStorageService = dataStorageService;
        this.landmarkDetectionService = landmarkDetectionService;
        this.mapsService = mapsService;
        this.pubsubService = pubsubService;
    }


    /**
     * Runs the worker, subscribing to the subscription and processing the received messages.
     */
    @Override
    public void run() {
        pubsubService.subscribe((String requestId, String photoName, String timestamp, String bucketName, String blobName) -> {
                    LandmarksLogger.logger.info("Received request: " + requestId);

                    try {
                        String imageUrl = dataStorageService.getImageLocation(bucketName, blobName);

                        // Store the metadata in the Firestore
                        metadataService.storeRequestMetadata(requestId, photoName, timestamp, imageUrl);

                        // Process the image
                        LandmarksLogger.logger.info("Processing image: " + imageUrl);
                        List<Landmark> landmarks = landmarkDetectionService.detectLandmarks(imageUrl);
                        LandmarksLogger.logger.info("Found " + landmarks.size() + " landmarks");

                        List<LandmarkMetadata> landmarkMetadataList = landmarks.stream().map(landmark -> {
                            // Get the map for the landmark
                            byte[] map = mapsService.getStaticMap(landmark.getLocation());
                            landmark.setMap(map);

                            // Store the map in the Cloud Storage
                            String mapBlobName = dataStorageService.storeLandmarkMap(landmark);
                            if (mapBlobName == null) {
                                LandmarksLogger.logger.info("Error storing map for landmark: " + landmark.getName());
                                return null;
                            }

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
        );
    }
}
