package pt.isel.cn.landmarks.app;

import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.services.datastorage.StorageService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.pubsub.LandmarksGooglePubsubService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Application for the detection of Landmarks.
 */
public class LandmarksApp {

    private final StorageService storageService;
    private final LandmarkDetectionService landmarkDetectionService;
    private final MapsService mapsService;
    private final LandmarksGooglePubsubService pubsubService;

    /**
     * Constructor for the app.
     *
     * @param storageService           the storage service
     * @param landmarkDetectionService the landmarks service
     * @param mapsService              the map service
     * @param pubsubService            the Pub/Sub service
     */
    public LandmarksApp(
            StorageService storageService,
            LandmarkDetectionService landmarkDetectionService,
            MapsService mapsService,
            LandmarksGooglePubsubService pubsubService
    ) {
        this.storageService = storageService;
        this.landmarkDetectionService = landmarkDetectionService;
        this.mapsService = mapsService;
        this.pubsubService = pubsubService;
    }


    /**
     * Starts the app, subscribing to the subscription and processing the received messages.
     */
    public void start() {
        pubsubService.subscribe((String requestId, String photoName, String timestamp, String bucketName, String blobName) -> {
                    LandmarksLogger.logger.info("Received request: " + requestId);

                    try {
                        String photoUrl = storageService.getPhotoUrl(bucketName, blobName);

                        // Store the metadata in the Firestore
                        storageService.storeRequestMetadata(requestId, photoName, timestamp, photoUrl);

                        // Process the photo
                        LandmarksLogger.logger.info("Processing photo: " + photoUrl);
                        List<Landmark> landmarks = landmarkDetectionService.detectLandmarks(photoUrl);
                        LandmarksLogger.logger.info("Found " + landmarks.size() + " landmarks");

                        List<LandmarkMetadata> landmarkMetadataList = landmarks.stream().map(landmark -> {
                                    // Get the map for the landmark
                                    byte[] map = mapsService.getStaticMap(landmark.getLocation());
                                    landmark.setMap(map);

                                    // Store the map in the Cloud Storage
                                    String mapBlobName = storageService.storeLandmarkMap(landmark);
                                    if (mapBlobName == null) {
                                        LandmarksLogger.logger.info("Error storing map for landmark: " + landmark.getName());
                                    }

                                    return new LandmarkMetadata(
                                            landmark.getName(),
                                            landmark.getLocation(),
                                            landmark.getConfidence(),
                                            mapBlobName
                                    );
                                })
                                .collect(Collectors.toList());

                        // Store the landmarks in the Firestore
                        storageService.storeLandmarksMetadata(requestId, landmarkMetadataList);

                        LandmarksLogger.logger.info("Finished processing request: " + requestId);
                    } catch (IOException e) {
                        LandmarksLogger.logger.info("I/O Error during processing of request: " + requestId);
                        e.printStackTrace();
                    }
                }
        );
    }
}
