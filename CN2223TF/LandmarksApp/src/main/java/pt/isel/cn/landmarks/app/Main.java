package pt.isel.cn.landmarks.app;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageService;
import pt.isel.cn.landmarks.app.services.landmarks.GoogleVisionLandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarkDetectionService;
import pt.isel.cn.landmarks.app.services.maps.GoogleMapsStaticService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.metadata.MetadataService;
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
     * Starts the app through a {@link LandmarksWorker}.
     * <p>
     * Is also responsible for creating the object instances used for dependency injection.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        CloudDataStorage cloudDataStorage = new GoogleCloudDataStorage(StorageOptions.getDefaultInstance().getService());
        MetadataStorage metadataStorage = new FirestoreMetadataStorage(FirestoreOptions.getDefaultInstance().getService());
        MetadataService metadataService = new MetadataService(metadataStorage);
        DataStorageService dataStorageService = new DataStorageService(cloudDataStorage);
        LandmarkDetectionService landmarkDetectionService = new GoogleVisionLandmarkDetectionService();
        MapsService mapsService = new GoogleMapsStaticService();
        GooglePubsubService googlePubsubService = new GooglePubsubService();
        LandmarksGooglePubsubService pubsubService = new LandmarksGooglePubsubService(googlePubsubService);

        LandmarksLogger.logger.info("LandmarksApp worker starting...");
        LandmarksWorker worker = new LandmarksWorker(
                metadataService,
                dataStorageService,
                landmarkDetectionService,
                mapsService,
                pubsubService
        );
        worker.run();
        LandmarksLogger.logger.info("LandmarksApp worker started.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
