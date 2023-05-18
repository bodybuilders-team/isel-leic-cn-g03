package pt.isel.cn.landmarks;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import pt.isel.cn.landmarks.services.landmarks.LandmarksService;
import pt.isel.cn.landmarks.services.landmarks.LandmarksVisionService;
import pt.isel.cn.landmarks.services.maps.MapsService;
import pt.isel.cn.landmarks.services.maps.MapsStaticService;
import pt.isel.cn.landmarks.services.pubsub.GooglePubsubService;
import pt.isel.cn.landmarks.services.pubsub.PubsubService;
import pt.isel.cn.landmarks.storage.data.CloudStorageService;
import pt.isel.cn.landmarks.storage.data.DataStorage;
import pt.isel.cn.landmarks.storage.metadata.FirestoreService;
import pt.isel.cn.landmarks.storage.metadata.MetadataStorage;

/**
 * Main class for the LandmarksApp.
 */
public class Main {

    public static final String PROJECT_ID = "cn2223-t1-g03";
    public static final String SUBSCRIPTION_ID = "landmarks-sub";

    /**
     * Entry point of the Landmarks application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Initialize dependencies
        DataStorage dataStorage = new CloudStorageService(StorageOptions.getDefaultInstance().getService());
        MetadataStorage metadataStorage = new FirestoreService(FirestoreOptions.getDefaultInstance().getService());
        LandmarksService landmarksService = new LandmarksVisionService();
        MapsService mapsService = new MapsStaticService();
        PubsubService pubsubService = new GooglePubsubService();

        LandmarksLogger.logger.info("LandmarksApp worker starting...");
        LandmarksWorker worker = new LandmarksWorker(
                dataStorage,
                metadataStorage,
                landmarksService,
                mapsService,
                pubsubService
        );
        worker.run();
        LandmarksLogger.logger.info("LandmarksApp worker started.");
    }
}
