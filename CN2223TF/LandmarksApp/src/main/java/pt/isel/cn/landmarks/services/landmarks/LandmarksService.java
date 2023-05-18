package pt.isel.cn.landmarks.services.landmarks;


import pt.isel.cn.landmarks.domain.Landmark;

import java.io.IOException;
import java.util.List;

/**
 * Processes images and performs landmark detection.
 */
public interface LandmarksService {

    /**
     * Processes the given image and performs landmark detection.
     *
     * @param imageLocation The location of the image to be processed.
     * @return a list of possible landmarks found in the image.
     */
    List<Landmark> detectLandmarks(String imageLocation) throws IOException;
}
