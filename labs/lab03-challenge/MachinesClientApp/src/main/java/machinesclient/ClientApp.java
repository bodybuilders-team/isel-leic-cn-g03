package machinesclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import machinesmanager.Information;
import machinesmanager.MachinesManagerContractGrpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Client application that sends temperature information to the server.
 * It also receives configuration information from the server.
 */
public class ClientApp {
    private static final int SVC_PORT = 8000;
    private static final String SVC_IP = "localhost";

    static StreamObserver<Information> serverStreamObserver;
    static boolean running = true;
    static boolean restarting = false;
    static Map<Integer, String> config = new HashMap<>();
    private static final int TEMPERATURE_THRESHOLD = 5000;

    /**
     * Sends the temperature information to the server periodically.
     */
    private static void sendTemperaturePeriodically() {
        Random random = new Random();
        new Thread(() -> {
            while (running) {
                double temperature = ((random.nextDouble() * 100) + 10);
                serverStreamObserver.onNext(Information.newBuilder()
                        .setTemperature(temperature)
                        .build()
                );

                try {
                    Thread.sleep(TEMPERATURE_THRESHOLD);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(SVC_IP, SVC_PORT)
                    .usePlaintext()
                    .build();
            MachinesManagerContractGrpc.MachinesManagerContractStub stub = MachinesManagerContractGrpc.newStub(channel);

            ClientStreamObserver clientStreamObserver = new ClientStreamObserver();
            serverStreamObserver = stub.connectToManager(clientStreamObserver);

            sendTemperaturePeriodically();

            while (running) {
                if (restarting) {
                    restarting = false;
                    running = true;
                    sendTemperaturePeriodically();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
