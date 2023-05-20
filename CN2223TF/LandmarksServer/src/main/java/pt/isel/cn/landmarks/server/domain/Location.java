package pt.isel.cn.landmarks.server.domain;

/**
 * Geographic location.
 */
public class Location {
    private double latitude;
    private double longitude;

    /**
     * Constructor for a location.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
