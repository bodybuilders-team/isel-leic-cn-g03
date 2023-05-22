package pt.isel.cn.landmarks.app.services.landmarks;


import pt.isel.cn.landmarks.app.domain.Landmark;

import java.io.IOException;
import java.util.List;

/**
 * Processes photos and performs landmark detection.
 */
public interface LandmarkDetectionService {

    /**
     * Processes the given photo and performs landmark detection.
     *
     * @param photoUri the uri of the photo to be processed
     * @return a list of possible landmarks found in the photo
     */
    List<Landmark> detectLandmarks(String photoUri) throws IOException;

    /**
     * Processes the given photo and performs landmark detection.
     *
     * @param photoBytes the photo to be processed in byte array form
     * @return a list of possible landmarks found in the photo
     */
    List<Landmark> detectLandmarks(byte[] photoBytes) throws IOException;
}
