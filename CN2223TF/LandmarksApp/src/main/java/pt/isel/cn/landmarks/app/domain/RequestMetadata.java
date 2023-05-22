package pt.isel.cn.landmarks.app.domain;

import com.google.cloud.Timestamp;

/**
 * Metadata for a landmark detection request.
 * <p>
 * Contains request information and the landmarks detected in the photo.
 */
public class RequestMetadata {
    private final String requestId;
    private final String photoName;
    private final Timestamp timestamp;
    private final String photoUrl;
    private final String status;
    private final LandmarkMetadata[] landmarks;

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
    public RequestMetadata(String requestId, String photoName, Timestamp timestamp, String photoUrl, String status, LandmarkMetadata[] landmarks) {
        this.requestId = requestId;
        this.photoName = photoName;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
        this.status = status;
        this.landmarks = landmarks;
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

    public LandmarkMetadata[] getLandmarks() {
        return landmarks;
    }
}
