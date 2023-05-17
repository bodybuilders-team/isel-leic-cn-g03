package pt.isel.cn.landmarks.model;

/**
 * A landmark.
 */
public class Landmark {
    private String name;
    private double latitude;
    private double longitude;
    private float confidence;

    public Landmark(String name, double latitude, double longitude, float confidence) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {return longitude;}

    public float getConfidence() {
        return confidence;
    }
}
