package pt.isel.cn.landmarks.service.map;

import pt.isel.cn.landmarks.domain.Location;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implements the {@link MapService} interface using the Google Maps Static API.
 *
 * @see <a href="https://developers.google.com/maps/documentation/maps-static/start#introduction">Google Maps Static API</a>
 */
public class MapServiceMapsStatic implements MapService {

    private static final String API_KEY = System.getenv("GOOGLE_MAPS_API_KEY");
    private static final String API_URL = "https://maps.googleapis.com/maps/api/staticmap?";

    private static final String DEFAULT_ZOOM = "15";
    private static final String DEFAULT_SIZE = "600x300";

    @Override
    public byte[] getStaticMap(Location location) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL
                            + "center=" + location.getLatitude() + "," + location.getLongitude()
                            + "&zoom=" + DEFAULT_ZOOM
                            + "&size=" + DEFAULT_SIZE
                            + "&key=" + API_KEY)
                    )
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Check if the response was successful
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                byte[] image = response.body().readAllBytes();
                response.body().close();
                return image;
            } else {
                System.out.println("Failed to retrieve the static map. Response code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
