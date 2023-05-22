package pt.isel.cn.landmarks.server;

import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import landmarks.LandmarksServiceGrpc;
import pt.isel.cn.landmarks.server.service.LandmarksDetector;
import pt.isel.cn.landmarks.server.service.PubSubLandmarksDetector;
import pt.isel.cn.landmarks.server.service.Service;
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
     * Starts the server for Landmarks gRPC service following {@link LandmarksServiceGrpc} contract.
     * <p>
     * Is also responsible for creating the object instances used for dependency injection.
     *
     * @param args the command line arguments
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        CloudDataStorage cloudDataStorage = new GoogleCloudDataStorage(StorageOptions.getDefaultInstance().getService());
        MetadataStorage metadataStorage = new FirestoreMetadataStorage(FirestoreOptions.getDefaultInstance().getService());
        LandmarksDetector landmarksDetector = new PubSubLandmarksDetector();
        Service service = new Service(cloudDataStorage, metadataStorage, landmarksDetector);

        Server svc = ServerBuilder.forPort(SVC_PORT)
                .addService(new LandmarksServer(service))
                .build()
                .start();
        System.out.println("LandmarksServer started on port " + SVC_PORT);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        svc.shutdown();
    }
}
