package pt.isel.cn.landmarks.server;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import landmarks.LandmarksServiceGrpc;
import pt.isel.cn.landmarks.server.service.images.ImageService;
import pt.isel.cn.landmarks.server.service.images.ImageServiceImpl;
import pt.isel.cn.landmarks.server.service.maps.MapService;
import pt.isel.cn.landmarks.server.storage.data.CloudDataStorage;
import pt.isel.cn.landmarks.server.storage.data.GoogleCloudDataStorage;
import pt.isel.cn.landmarks.server.storage.metadata.FirestoreMetadataStorage;
import pt.isel.cn.landmarks.server.storage.metadata.MetadataStorage;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main class for the LandmarksServer.
 */
public class Main {

    private static final int SVC_PORT = 8000;

    /**
     * Entry point for the Landmarks Server.
     * <p>
     * Starts the server for Landmarks GRPC service following {@link LandmarksServiceGrpc} contract.
     * <p>
     * Is also responsible for creating the object instances used for dependency injection.
     *
     * @param args the command line arguments
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        CloudDataStorage cloudDataStorage = new GoogleCloudDataStorage(StorageOptions.getDefaultInstance().getService());
        ImageService imageService = new ImageServiceImpl(cloudDataStorage);
        MapService mapService = new MapService(cloudDataStorage);
        MetadataStorage metadataStorage = new FirestoreMetadataStorage(FirestoreOptions.getDefaultInstance().getService());

        Server svc = ServerBuilder.forPort(SVC_PORT)
                .addService(new LandmarksServer(imageService, mapService, metadataStorage))
                .build()
                .start();
        System.out.println("LandmarksServer started on port " + SVC_PORT);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        svc.shutdown();
    }
}
