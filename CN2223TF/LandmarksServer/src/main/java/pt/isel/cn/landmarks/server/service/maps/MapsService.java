package pt.isel.cn.landmarks.server.service.maps;

import pt.isel.cn.landmarks.server.domain.Location;

/**
 * Obtains static maps based on locations.
 */
public interface MapsService {

    /**
     * Retrieves a static map image based on the provided location.
     *
     * @param location The location for which to retrieve the static map.
     * @return The static map image as a Result object.
     */
    byte[] getStaticMap(Location location);
}

