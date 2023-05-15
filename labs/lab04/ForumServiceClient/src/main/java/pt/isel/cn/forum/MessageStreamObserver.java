package pt.isel.cn.forum;

import forum.ForumMessage;
import io.grpc.stub.StreamObserver;

/**
 * Stream observer for the messages received from the forum service.
 */
public class MessageStreamObserver implements StreamObserver<ForumMessage> {

    private static final String DOWNLOAD_BLOBS_DESTINATION = "downloadedBlobs/";
    private final StorageOperations soper;
    private boolean isCompleted = false;
    private boolean success = false;

    public MessageStreamObserver(StorageOperations soper) {
        this.soper = soper;
    }

    /**
     * Returns true if the subscription was successful.
     *
     * @return true if the subscription was successful.
     */
    public boolean OnSuccesss() {return success;}

    /**
     * Returns true if the subscription was completed.
     *
     * @return true if the subscription was completed.
     */
    public boolean isCompleted() {return isCompleted;}

    @Override
    public void onNext(ForumMessage forumMessage) { // <texto>[;<bucketName>;<blobName>
        String[] message = forumMessage.getTxtMsg().split(";");
        String text = message[0].substring(0, message[0].length() - 1);
        String bucketName = message.length >= 2 ? message[1] : null;
        String blobName = message.length >= 3 ? message[2].substring(0, message[2].length() - 1) : null;

        System.out.println("Received message: " + text +
                " from " + forumMessage.getFromUser() +
                " on topic " + forumMessage.getTopicName());

        if (bucketName != null && blobName != null) {
            try {
                soper.downloadBlobFromBucket(bucketName, blobName, DOWNLOAD_BLOBS_DESTINATION + blobName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:" + throwable.getMessage());
        throwable.printStackTrace();
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
