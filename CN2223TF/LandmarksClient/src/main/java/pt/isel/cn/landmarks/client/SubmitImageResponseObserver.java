package pt.isel.cn.landmarks.client;

import io.grpc.stub.StreamObserver;
import landmarks.SubmitImageResponse;

/**
 * Stream observer to handle the response of the SubmitImage RPC.
 */
class SubmitImageResponseObserver implements StreamObserver<SubmitImageResponse> {

    @Override
    public void onNext(SubmitImageResponse response) {
        System.out.println("Image submitted successfully. Request ID: " + response.getRequestId());
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Failed to submit image: " + t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Image submission completed.");
    }
}
