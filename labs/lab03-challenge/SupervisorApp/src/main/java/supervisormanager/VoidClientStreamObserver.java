package supervisormanager;

import io.grpc.stub.StreamObserver;

public class VoidClientStreamObserver implements StreamObserver<Void> {
    private boolean isCompleted = false;
    private boolean success = false;

    public boolean OnSuccess() {
        return success;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(Void aVoid) {
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:" + throwable.getMessage());
        isCompleted = true;
        success = false;
    }

    @Override
    public void onCompleted() {
        // System.out.println("Stream completed");
        isCompleted = true;
        success = true;
    }
}
