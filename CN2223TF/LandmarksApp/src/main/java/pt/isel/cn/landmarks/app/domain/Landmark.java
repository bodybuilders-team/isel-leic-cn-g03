package pt.isel.cn.landmarks.app.domain;

/**
 * A landmark detected in an image.
 * <p>
 * A landmark represents a real-world point of interest.
 * Contains the name and location of the landmark, the confidence score for the
 * detection of the landmark, and may contain the detected landmark map.
 */
public class Landmark {
    private final String name;
    private final Location location;
    private final float confidence;
    private byte[] map;

    /**
     * Constructor for a landmark still without an associated map image.
     *
     * @param name       The name of the landmark.
     * @param location   The location of the landmark.
     * @param confidence The confidence score for the detection of the landmark.
     */
    public Landmark(String name, Location location, float confidence) {
        this.name = name;
        this.location = location;
        this.confidence = confidence;
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

    public byte[] getMap() {
        return map;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }
}
