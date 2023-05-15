package supervisormanager;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Client application for the supervisor.
 */
public class ClientApp {
    private static final int SVC_PORT = 8000;
    private static final String SVC_IP = "localhost";

    public static SupervisorManagerGrpc.SupervisorManagerStub stub;
    private static ManagedChannel channel;

    public static void main(String[] args) {
        try {
            channel = ManagedChannelBuilder.forAddress(SVC_IP, SVC_PORT)
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
