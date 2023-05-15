package supervisormanager;

import io.grpc.stub.StreamObserver;

import static supervisormanager.ClientApp.stub;

/**
 * Stream observer for the client application of the supervisor.
 */
public class ClientStreamObserver implements StreamObserver<AllMachineIDs> {
    private boolean isCompleted = false;
    private boolean success = false;

    /**
     * Returns true if the machine was successfully connected to the server.
     *
     * @return true if the machine was successfully connected to the server
     */
    public boolean OnSuccess() {
        return success;
    }

    /**
     * Returns true if the stream is completed.
     *
     * @return true if the stream is completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(AllMachineIDs allMachineIDs) {
        stub.sendCommandToMachine(
                Command.newBuilder()
                        .setId(allMachineIDs.getIDsList().get(0))
                        .setCtlNumber(0)
                        .setCtltext("stop")
                        .build(),
                new VoidClientStreamObserver()
        );
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:" + throwable.getMessage());
        isCompleted = true;
        success = false;
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
        isCompleted = true;
        success = true;
    }
}
