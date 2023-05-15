package pt.isel.cn.temporary_occupation;

import com.google.cloud.firestore.GeoPoint;

/**
 * Location.
 */
public class Location {
    public GeoPoint point;
    public Coordinates coord;
    public String parish;
    public String local;

    public Location() {
        // Empty constructor
    }
}
