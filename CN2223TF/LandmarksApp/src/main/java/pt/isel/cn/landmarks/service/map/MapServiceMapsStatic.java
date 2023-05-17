package pt.isel.cn.landmarks.service.map;

import pt.isel.cn.landmarks.model.Location;

/**
 * Implements the {@link MapService} interface using the Google Maps Static API.
 *
 * @see <a href="https://developers.google.com/maps/documentation/maps-static/start#introduction">Google Maps Static API</a>
 */
public class MapServiceMapsStatic implements MapService {

    @Override
    public Result getStaticMap(Location location) {
        return null;
    }
}
