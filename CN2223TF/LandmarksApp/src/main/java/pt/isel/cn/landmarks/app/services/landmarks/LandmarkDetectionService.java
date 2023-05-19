package pt.isel.cn.landmarks.app.services.landmarks;


import pt.isel.cn.landmarks.app.domain.Landmark;

import java.io.IOException;
import java.util.List;

/**
 * Processes images and performs landmark detection.
 */
public interface LandmarkDetectionService {

    /**
     * Processes the given image and performs landmark detection.
     *
     * @param imageUri the uri of the image to be processed
     * @return a list of possible landmarks found in the image
     */
    List<Landmark> detectLandmarks(String imageUri) throws IOException;

    /**
     * Processes the given image and performs landmark detection.
     *
     * @param imageBytes the image to be processed in byte array form
     * @return a list of possible landmarks found in the image
     */
    List<Landmark> detectLandmarks(byte[] imageBytes) throws IOException;
}
