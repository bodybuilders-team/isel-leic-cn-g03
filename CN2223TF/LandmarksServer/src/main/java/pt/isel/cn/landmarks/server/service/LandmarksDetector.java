package pt.isel.cn.landmarks.server.service;

import pt.isel.cn.landmarks.server.service.exceptions.LandmarkDetectionException;

public interface LandmarksDetector {

    /**
     * Notifies the landmarks detector about a photo, which may in turn trigger the detection of landmarks in the photo.
     *
     * @param requestId the id of the request
     * @param photoName the name of the photo
     * @param blobName  the blob name of the uploaded photo
     */
    void notifyAboutRequest(String requestId, String photoName, String blobName) throws LandmarkDetectionException;
}
