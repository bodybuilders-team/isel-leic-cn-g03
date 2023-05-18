package pt.isel.cn.landmarks.storage.metadata;

import com.google.cloud.firestore.Firestore;
import pt.isel.cn.landmarks.domain.Landmark;
import pt.isel.cn.landmarks.domain.Request;

import java.util.HashMap;
import java.util.Map;

public class FirestoreService implements MetadataStorage {

    private final Firestore service;
    private static final String COLLECTION_NAME = "landmarks"; // TODO: review names of collections/topics/buckets, etc

    public FirestoreService(Firestore service) {
        this.service = service;
    }

    /*
    Document -> Request
      - request-id                  (Server generated)
      - timestamp                   (Server generated)
      - image-url                   (Client provided)
      - status: [PENDING, DONE]     (Server generated)
      - landmarks: [Landmark]       (Landmarks detected by the worker)
     */
    @Override
    public void storeRequestMetadata(String requestId, String imageUrl) {
        Request request = new Request(
                requestId,
                System.currentTimeMillis(),
                imageUrl,
                "PENDING",
                null
        );

        service.collection(COLLECTION_NAME)
                .document(requestId)
                .set(request);
    }

    @Override
    public void storeLandmarkMetadata(String requestId, Landmark landmark) {
        Map<String, Object> landmarkMap = new HashMap<>(); // Maybe create a class for this
        landmarkMap.put("name", landmark.getName());
        landmarkMap.put("location", landmark.getLocation());
        landmarkMap.put("confidence", landmark.getConfidence());

        service.collection(COLLECTION_NAME)
                .document(requestId)
                .update("landmarks", landmarkMap,
                        "status", "DONE"
                );
    }
}
