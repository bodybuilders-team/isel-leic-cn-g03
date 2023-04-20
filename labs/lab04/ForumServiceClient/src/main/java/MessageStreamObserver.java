import forum.ForumMessage;
import io.grpc.stub.StreamObserver;

public class MessageStreamObserver implements StreamObserver<ForumMessage> {

    private static final String downloadBlobsDestination = "../downloadedBlobs/";
    private final StorageOperations soper;
    private boolean isCompleted = false;
    private boolean success = false;

    public MessageStreamObserver(StorageOperations soper) {
        this.soper = soper;
    }

    public boolean OnSuccesss() {return success;}

    public boolean isCompleted() {return isCompleted;}

    @Override
    public void onNext(ForumMessage forumMessage) { // <texto>[;<bucketName>;<blobName>]
        String[] message = forumMessage.getTxtMsg().split(";");
        String text = message[0];
        String bucketName = message[1];
        String blobName = message[2];

        System.out.println("Received message:" + text +
                " from " + forumMessage.getFromUser() +
                " on topic " + forumMessage.getTopicName());

        if (bucketName != null && blobName != null) {
            try {
                soper.downloadBlobFromBucket(bucketName, blobName, downloadBlobsDestination + blobName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
