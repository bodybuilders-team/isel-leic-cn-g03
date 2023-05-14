package primesclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import primesservice.PrimesInterval;
import primesservice.PrimesServiceGrpc;
import primesservice.Text;
import primesservice.Void;

import java.util.Scanner;

/**
 * Client for the primes service using gRPC.
 */
public class ClientApp {

    private static final String svcIP = "localhost";
    private static final int svcPort = 8000;

    private static PrimesServiceGrpc.PrimesServiceBlockingStub blockingStub;
    private static PrimesServiceGrpc.PrimesServiceStub nonblockingStub;

    /**
     * Calls the isAlive service.
     *
     * @param request the request
     */
    static void isAlive(Void request) {
        Text text = blockingStub.isAlive(request);
        System.out.println("isAlive: " + text.getMsg());
    }

    /**
     * Calls the findPrimes service.
     *
     * @param request          the request
     * @param responseObserver the response observer
     */
    static void findPrimes(PrimesInterval request, ClientStreamObserver responseObserver) {
        nonblockingStub.findPrimes(request, responseObserver);
    }

    /**
     * Entry point of the client application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Channels are secure by default (via SSL/TLS).
        // For the example we disable TLS to avoid needing certificates.
        ManagedChannel channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                // Channels are secure by default (via SSL/TLS).
                // For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build();

        blockingStub = PrimesServiceGrpc.newBlockingStub(channel);
        nonblockingStub = PrimesServiceGrpc.newStub(channel);

        isAlive(Void.newBuilder().build());

        int start = 1;
        int end = 500;
        int interval = 100;

        for (int i = start; i <= end; i += interval) {
            findPrimes(
                    PrimesInterval.newBuilder()
                            .setStartNum(i)
                            .setEndNum(i + interval)
                            .build(),
                    new ClientStreamObserver()
            );
        }

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
