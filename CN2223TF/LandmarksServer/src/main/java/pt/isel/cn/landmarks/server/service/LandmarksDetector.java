package pt.isel.cn.landmarks.server.service;

import pt.isel.cn.landmarks.server.service.exceptions.LandmarkDetectionException;

public interface LandmarksDetector {

    /**
     * Notifies the landmarks detector about an image, which may in turn trigger the detection of landmarks in the image.
     *
     * @param requestId     the id of the request
     * @param photoName     the name of the photo
     * @param imageLocation the location of the uploaded image
     */
    void notifyAboutRequest(String requestId, String photoName, String imageLocation) throws LandmarkDetectionException;
}
