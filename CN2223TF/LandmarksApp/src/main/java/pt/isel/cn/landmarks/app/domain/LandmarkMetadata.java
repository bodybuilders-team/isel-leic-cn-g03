package pt.isel.cn.landmarks.app.domain;

/**
 * Metadata for the landmark detected in an image.
 * <p>
 * A landmark represents a real-world point of interest.
 * Contains the name and location of the landmark, the confidence score for the
 * detection of the landmark, and may contain the detected landmark map blob name.
 */
public class LandmarkMetadata {
    private final String name;
    private final Location location;
    private final float confidence;
    private final String mapBlobName;

    /**
     * Constructor for a landmark metadata.
     *
     * @param name        the name of the landmark
     * @param location    the location of the landmark
     * @param confidence  the confidence score for the detection of the landmark
     * @param mapBlobName the name of the blob containing the landmark map
     */
    public LandmarkMetadata(String name, Location location, float confidence, String mapBlobName) {
        this.name = name;
        this.location = location;
        this.confidence = confidence;
        this.mapBlobName = mapBlobName;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getMapBlobName() {
        return mapBlobName;
    }
}