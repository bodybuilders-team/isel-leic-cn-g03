package supervisormanager;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ClientApp {
    private static final int svcPort = 8000;
    private static final String svcIP = "localhost";
    public static SupervisorManagerGrpc.SupervisorManagerStub stub;
    private static ManagedChannel channel;

    public static void main(String[] args) {
        try {
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    .usePlaintext()
                    .build();
            stub = SupervisorManagerGrpc.newStub(channel);

            ClientStreamObserver clientStreamObserver = new ClientStreamObserver();

            stub.getMachinesIds(Void.newBuilder().build(), clientStreamObserver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
