package pt.isel.cn.landmarks.service.landmarks;


import pt.isel.cn.landmarks.model.Landmark;

import java.io.IOException;
import java.util.List;

/**
 * Processes images and performs landmark analysis.
 */
public interface LandmarksService {

    /**
     * Processes the given image and performs landmark analysis.
     *
     * @param imageLocation The location of the image to be processed.
     * @return a list of landmarks found in the image.
     */
    List<Landmark> detectLandmarks(String imageLocation) throws IOException;
}
