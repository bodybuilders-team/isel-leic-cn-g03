package pt.isel.cn.landmarks.app;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import pt.isel.cn.landmarks.app.services.datastorage.StorageService;
import pt.isel.cn.landmarks.app.services.landmarks.GoogleVisionLandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.maps.GoogleMapsStaticService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.pubsub.GooglePubsubService;
import pt.isel.cn.landmarks.app.services.pubsub.LandmarksGooglePubsubService;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.app.storage.data.GoogleCloudDataStorage;
import pt.isel.cn.landmarks.app.storage.metadata.FirestoreMetadataStorage;
import pt.isel.cn.landmarks.app.storage.metadata.MetadataStorage;

import java.util.Scanner;

/**
 * Main class for the LandmarksApp.
 */
public class Main {

    /**
     * Entry point of the Landmarks application.
     * <p>
     * Starts the app through a {@link LandmarksApp}.
     * <p>
     * Is also responsible for creating the object instances used for dependency injection.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        CloudDataStorage cloudDataStorage = new GoogleCloudDataStorage(StorageOptions.getDefaultInstance().getService());
        MetadataStorage metadataStorage = new FirestoreMetadataStorage(FirestoreOptions.getDefaultInstance().getService());
        StorageService storageService = new StorageService(cloudDataStorage, metadataStorage);
        LandmarkDetectionService landmarkDetectionService = new GoogleVisionLandmarkDetectionService();
        MapsService mapsService = new GoogleMapsStaticService();
        GooglePubsubService googlePubsubService = new GooglePubsubService();
        LandmarksGooglePubsubService pubsubService = new LandmarksGooglePubsubService(googlePubsubService);

        LandmarksLogger.logger.info("LandmarksApp starting...");
        LandmarksApp app = new LandmarksApp(
                storageService,
                landmarkDetectionService,
                mapsService,
                pubsubService
        );
        app.start();
        LandmarksLogger.logger.info("LandmarksApp started.");
    }
}
