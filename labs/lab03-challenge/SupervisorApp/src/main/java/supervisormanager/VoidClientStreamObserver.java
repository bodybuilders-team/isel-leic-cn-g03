package supervisormanager;

import io.grpc.stub.StreamObserver;

/**
 * Stream observer for the supervisor.
 */
public class VoidClientStreamObserver implements StreamObserver<Void> {
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
        System.out.println("Stream completed");
        isCompleted = true;
        success = true;
    }
}
