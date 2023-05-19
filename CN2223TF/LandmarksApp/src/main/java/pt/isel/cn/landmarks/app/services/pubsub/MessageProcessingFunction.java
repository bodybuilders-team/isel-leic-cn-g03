package pt.isel.cn.landmarks.app.services.pubsub;

/**
 * Functional interface representing the processing function.
 */
@FunctionalInterface
public interface MessageProcessingFunction {
    void processRequest(String requestId, String photoName, String timestamp, String bucketName, String blobName);
}
