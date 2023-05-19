package pt.isel.cn.landmarks.app;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageService;
import pt.isel.cn.landmarks.app.services.datastorage.DataStorageServiceImpl;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarksService;
import pt.isel.cn.landmarks.app.services.landmarks.LandmarksVisionService;
import pt.isel.cn.landmarks.app.services.maps.MapsService;
import pt.isel.cn.landmarks.app.services.maps.MapsStaticService;
import pt.isel.cn.landmarks.app.services.metadatastorage.MetadataService;
import pt.isel.cn.landmarks.app.services.pubsub.GooglePubsubService;
import pt.isel.cn.landmarks.app.services.pubsub.PubsubService;
import pt.isel.cn.landmarks.app.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.app.storage.data.GoogleCloudDataStorage;
import pt.isel.cn.landmarks.app.storage.metadata.FirestoreMetadataStorage;
import pt.isel.cn.landmarks.app.storage.metadata.MetadataStorage;

/**
 * Main class for the LandmarksApp.
 */
public class Main {

    public static final String PROJECT_ID = "cn2223-t1-g03";
    public static final String SUBSCRIPTION_ID = "landmarks-sub";

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
        DataStorageService dataStorageService = new DataStorageServiceImpl(cloudDataStorage);
        LandmarksService landmarksService = new LandmarksVisionService();
        MapsService mapsService = new MapsStaticService();
        PubsubService pubsubService = new GooglePubsubService();

        LandmarksLogger.logger.info("LandmarksApp worker starting...");
        LandmarksWorker worker = new LandmarksWorker(
                metadataService,
                dataStorageService,
                landmarksService,
                mapsService,
                pubsubService
        );
        worker.run();
        LandmarksLogger.logger.info("LandmarksApp worker started.");
    }
}
