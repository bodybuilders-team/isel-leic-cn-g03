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
 * it is used to detect landmarks in images.
 *
 * @see <a href="https://cloud.google.com/vision">Google Vision API</a>
 */
public class GoogleVisionLandmarkDetectionService implements LandmarkDetectionService {

    @Override
    public List<Landmark> detectLandmarks(String imageLocation) throws IOException {
        List<Landmark> landmarks = new ArrayList<>();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(imageLocation).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
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
