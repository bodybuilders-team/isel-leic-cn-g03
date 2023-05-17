package pt.isel.cn.landmarks.service.map;

import pt.isel.cn.landmarks.model.Location;

/**
 * Obtains static maps based on locations.
 */
public interface MapService {

    /**
     * Retrieves a static map image based on the provided location.
     *
     * @param location The location for which to retrieve the static map.
     * @return The static map image as a Result object.
     */
    Result getStaticMap(Location location);
}

