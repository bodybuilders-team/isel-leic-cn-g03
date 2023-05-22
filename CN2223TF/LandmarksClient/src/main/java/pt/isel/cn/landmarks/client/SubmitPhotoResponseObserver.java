package pt.isel.cn.landmarks.client;

import io.grpc.stub.StreamObserver;
import landmarks.SubmitPhotoResponse;

/**
 * Stream observer to handle the response of the SubmitPhoto RPC.
 */
class SubmitPhotoResponseObserver implements StreamObserver<SubmitPhotoResponse> {

    @Override
    public void onNext(SubmitPhotoResponse response) {
        System.out.println("Photo submitted successfully. Request ID: " + response.getRequestId());
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Failed to submit photo: " + t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Photo submission completed.");
    }
}
