package primesclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import primesservice.*;
import primesservice.Void;

import java.util.List;

public class ClientApp {
    private static final int svcPort = 8000;
    private static final String svcIP = "34.175.247.249";

    private static ManagedChannel channel;
    private static PrimesServiceGrpc.PrimesServiceBlockingStub blockingStub;
    private static PrimesServiceGrpc.PrimesServiceStub nonBlockingStub;

    static void isAliveBlocking() {
        Text text = blockingStub.isAlive(Void.newBuilder().build());
        System.out.println(text.getMsg());
    }

    static void findPrimesNonBlocking(PrimesInterval primesInterval, ClientStreamObserver clientStreamObserver) {
        nonBlockingStub.findPrimes(primesInterval, clientStreamObserver);
    }

    public static void main(String[] args) {
        try {
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    .usePlaintext()
                    .build();
            blockingStub = PrimesServiceGrpc.newBlockingStub(channel);
            nonBlockingStub = PrimesServiceGrpc.newStub(channel);

//            System.out.print("Insert start number: ");
//            int startNum = new Scanner(System.in).nextInt();
//            System.out.print("Insert end number: ");
//            int endNum = new Scanner(System.in).nextInt();
            int startNum = 1;
            int endNum = 500;
            int interval = 100;

            List<ClientStreamObserver> clientStreamObservers = new java.util.ArrayList<>();

            for(int i = startNum; i <= endNum; i += interval) {
                ClientStreamObserver clientStreamObserver = new ClientStreamObserver();
                clientStreamObservers.add(clientStreamObserver);

                findPrimesNonBlocking(PrimesInterval.newBuilder()
                                .setStartNum(i)
                                .setEndNum(i + interval)
                                .build(),
                        clientStreamObserver);
            }

            for(ClientStreamObserver clientStreamObserver : clientStreamObservers) {
                while (!clientStreamObserver.isCompleted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
