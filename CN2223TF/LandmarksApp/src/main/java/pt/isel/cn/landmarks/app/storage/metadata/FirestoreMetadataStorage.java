package pt.isel.cn.landmarks.app.storage.metadata;

import com.google.cloud.firestore.Firestore;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.domain.RequestMetadata;

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
    public void storeRequestMetadata(RequestMetadata requestMetadata) {
        service.collection(COLLECTION_NAME)
                .document(requestMetadata.getRequestId())
                .set(requestMetadata);
    }

    @Override
    public void storeLandmarksMetadata(String requestId, List<LandmarkMetadata> landmarks) {
        service.collection(COLLECTION_NAME)
                .document(requestId)
                .update("landmarks", landmarks,
                        "status", "DONE"
                );
    }
}
