package pt.isel.cn.landmarks.app.services.landmarks;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.protobuf.ByteString;
import pt.isel.cn.landmarks.app.LandmarksLogger;
import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@link LandmarkDetectionService} using the Google Vision API.
 * <p>
 * Google Vision API is a service that allows to detect a variety of different things in images. In this case,
 * it is only used to detect landmarks in photos.
 *
 * @see <a href="https://cloud.google.com/vision">Google Vision API</a>
 */
public class GoogleVisionLandmarkDetectionService implements LandmarkDetectionService {

    /**
     * {@inheritDoc}
     * <p>
     * {@link ImageSource.Builder#setImageUri} is used instead of {@link ImageSource.Builder#setGcsImageUri}
     * because the latter only supports Google Cloud Storage URIs.
     */
    @Override
    public List<Landmark> detectLandmarks(String photoUri) throws IOException {
        return detectLandmarks(ImageSource.newBuilder().setImageUri(photoUri).build());
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@link ImageSource.Builder#setImageUriBytes} is used instead of {@link ImageSource.Builder#setGcsImageUriBytes}
     * because the latter only supports Google Cloud Storage URIs.
     */
    @Override
    public List<Landmark> detectLandmarks(byte[] photoBytes) throws IOException {
        return detectLandmarks(ImageSource.newBuilder().setImageUriBytes(ByteString.copyFrom(photoBytes)).build());
    }

    /**
     * Detects landmarks in the photo specified by the given {@link ImageSource}, that can be initialized with a photo
     * URI or with a byte array.
     *
     * @param imageSource the image to be processed
     * @return a list of possible landmarks found in the photo
     * @throws IOException if an I/O error occurs
     */
    private List<Landmark> detectLandmarks(ImageSource imageSource) throws IOException {
        List<Landmark> landmarks = new ArrayList<>();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        Image img = Image.newBuilder().setSource(imageSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError())
                    LandmarksLogger.logger.severe("Error: " + res.getError().getMessage());

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
                    LocationInfo info = annotation.getLocationsList().listIterator().next();
                    landmarks.add(new Landmark(
                            annotation.getDescription(),
                            new Location(info.getLatLng().getLatitude(), info.getLatLng().getLongitude()),
                            annotation.getScore()
                    ));
                }
            }
        }

        return landmarks;
    }
}
