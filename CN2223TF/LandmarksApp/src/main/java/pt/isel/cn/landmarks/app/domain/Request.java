package pt.isel.cn.landmarks.app.domain;

import com.google.cloud.Timestamp;

/**
 * A landmark detection request.
 * <p>
 * Contains request information and the landmarks detected in the image.
 */
public class Request {
    private final String requestId;
    private final Timestamp timestamp;
    private final String imageUrl;
    private final String status;
    private final Landmark[] landmarks;

    /**
     * Constructor for a landmark detection request.
     *
     * @param requestId The id of the request.
     * @param timestamp The timestamp of the request.
     * @param imageUrl  The url of the image.
     * @param status    The status of the request.
     * @param landmarks The landmarks detected in the image, after the request is processed.
     */
    public Request(String requestId, Timestamp timestamp, String imageUrl, String status, Landmark[] landmarks) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.status = status;
        this.landmarks = landmarks;
    }

    public String getRequestId() {
        return requestId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public Landmark[] getLandmarks() {
        return landmarks;
    }
}
