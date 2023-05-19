package pt.isel.cn.landmarks.app.services.maps;

import pt.isel.cn.landmarks.app.domain.Location;

/**
 * Obtains static maps based on locations.
 */
public interface MapsService {

    /**
     * Retrieves a static map image based on the provided location.
     *
     * @param location the location for which to retrieve the static map
     * @return the static map image as a byte array
     */
    byte[] getStaticMap(Location location);
}

