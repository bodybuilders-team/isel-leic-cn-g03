package pt.isel.cn.landmarks.server.domain;

import com.google.cloud.Timestamp;

import java.util.List;

/**
 * Metadata for a landmark detection request.
 * <p>
 * Contains request information and the landmarks detected in the image.
 */
public class RequestMetadata {
    private String requestId;
    private String photoName;
    private Timestamp timestamp;
    private String imageUrl;
    private String status;
    private List<LandmarkMetadata> landmarks;

    /**
     * Constructor for a landmark detection request.
     *
     * @param requestId the id of the request
     * @param photoName the name of the photo
     * @param timestamp the timestamp of the request
     * @param imageUrl  the url of the image
     * @param status    the status of the request
     * @param landmarks the landmarks detected in the image, after the request is processed
     */
    public RequestMetadata(String requestId, String photoName, Timestamp timestamp, String imageUrl, String status, List<LandmarkMetadata> landmarks) {
        this.requestId = requestId;
        this.photoName = photoName;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.status = status;
        this.landmarks = landmarks;
    }

    public RequestMetadata() {
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPhotoName() {
        return photoName;
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

    public List<LandmarkMetadata> getLandmarks() {
        return landmarks;
    }
}
