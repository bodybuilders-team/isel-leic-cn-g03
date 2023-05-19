package pt.isel.cn.landmarks.app.storage.metadata;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import pt.isel.cn.landmarks.app.domain.Landmark;
import pt.isel.cn.landmarks.app.domain.Request;

import java.util.List;

/**
 * Implementation of the {@link MetadataStorage} interface that uses Firestore.
 */
public class FirestoreMetadataStorage implements MetadataStorage {

    private static final String COLLECTION_NAME = "landmarks"; // TODO: review names of collections/topics/buckets, etc
    private final Firestore service;

    public FirestoreMetadataStorage(Firestore service) {
        this.service = service;
    }

    @Override
    public void storeRequestMetadata(String requestId, String timestamp, String imageUrl) {
        Request request = new Request(
                requestId,
                Timestamp.parseTimestamp(timestamp),
                imageUrl,
                "PENDING",
                null
        );

        service.collection(COLLECTION_NAME)
                .document(requestId)
                .set(request);
    }

    @Override
    public void storeLandmarksMetadata(String requestId, List<Landmark> landmarks) {
        service.collection(COLLECTION_NAME)
                .document(requestId)
                .update("landmarks", landmarks,
                        "status", "DONE"
                );
    }
}
