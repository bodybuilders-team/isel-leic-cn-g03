package supervisormanager;

import io.grpc.stub.StreamObserver;

import static supervisormanager.ClientApp.stub;

public class ClientStreamObserver implements StreamObserver<AllMachineIDs> {
    private boolean isCompleted = false;
    private boolean success = false;

    public boolean OnSuccess() {
        return success;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(AllMachineIDs allMachineIDs) {
        // Stop the first machine
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
