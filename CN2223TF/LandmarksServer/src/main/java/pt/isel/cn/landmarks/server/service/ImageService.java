package pt.isel.cn.landmarks.server.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ImageService {

    /**
     * Uploads an image.
     *
     * @param imageBytes the image in byte array form
     * @return the location of the uploaded image
     */
    String uploadImage(byte[] imageBytes);

    /**
     * Notifies the app that an image was uploaded.
     *
     * @param requestId     the id of the request
     * @param imageLocation the location of the uploaded image
     */
    void notifyImageUploaded(String requestId, String imageLocation) throws IOException, ExecutionException, InterruptedException;
}
