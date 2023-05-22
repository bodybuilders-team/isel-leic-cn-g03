package pt.isel.cn.landmarks.server.domain;

import com.google.cloud.Timestamp;

import java.util.List;

/**
 * Metadata for a landmark detection request.
 * <p>
 * Contains request information and the landmarks detected in the photo.
 */
public class RequestMetadata {
    private String requestId;
    private String photoName;
    private Timestamp timestamp;
    private String photoUrl;
    private String status;
    private List<LandmarkMetadata> landmarks;

    /**
     * Constructor for a landmark detection request.
     *
     * @param requestId the id of the request
     * @param photoName the name of the photo
     * @param timestamp the timestamp of the request
     * @param photoUrl  the url of the photo
     * @param status    the status of the request
     * @param landmarks the landmarks detected in the photo, after the request is processed
     */
    public RequestMetadata(String requestId, String photoName, Timestamp timestamp, String photoUrl, String status, List<LandmarkMetadata> landmarks) {
        this.requestId = requestId;
        this.photoName = photoName;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getStatus() {
        return status;
    }

    public List<LandmarkMetadata> getLandmarks() {
        return landmarks;
    }
}
