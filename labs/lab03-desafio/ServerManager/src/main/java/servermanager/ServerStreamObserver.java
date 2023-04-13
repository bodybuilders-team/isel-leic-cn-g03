package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Information;

import java.util.ArrayList;
import java.util.List;

public class ServerStreamObserver implements StreamObserver<Information> {
    private boolean isCompleted = false;
    private boolean success = false;

    public boolean OnSuccess() {
        return success;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(Information information) {
        System.out.println("Received information from: " + information.getMID().getID());

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
