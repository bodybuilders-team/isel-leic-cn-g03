package pt.isel.cn.landmarks.app.services.pubsub;

/**
 * Functional interface representing the processing function for Pub/sub messages of Landmarks.
 */
@FunctionalInterface
public interface LandmarksMessageProcessingFunction {
    void processRequest(String requestId, String photoName, String timestamp, String bucketName, String blobName);
}
