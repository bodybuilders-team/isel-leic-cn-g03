package machinesclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import machinesmanager.Information;
import machinesmanager.MachinesManagerContractGrpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientApp {
    private static final int svcPort = 8000;
    private static final String svcIP = "localhost";
    public static StreamObserver<Information> serverStreamObserver;
    public static boolean running = true;
    public static boolean restarting = false;
    public static Map<Integer, String> config = new HashMap<>();
    private static ManagedChannel channel;
    private static MachinesManagerContractGrpc.MachinesManagerContractStub stub;

    private static void sendTemperaturePeriodically() {
        new Thread(() -> {
            while (running) {
                double temperature = ((new Random().nextDouble() * 100) + 10);

                serverStreamObserver.onNext(Information.newBuilder()
                        .setTemperature(temperature)
                        .build()
                );

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    .usePlaintext()
                    .build();
            stub = MachinesManagerContractGrpc.newStub(channel);

            ClientStreamObserver clientStreamObserver = new ClientStreamObserver();

            serverStreamObserver = stub.connectToManager(clientStreamObserver);

            sendTemperaturePeriodically();

            while (true) {
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
