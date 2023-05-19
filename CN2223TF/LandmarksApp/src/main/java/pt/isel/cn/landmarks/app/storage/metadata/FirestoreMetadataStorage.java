package pt.isel.cn.landmarks.app.storage.metadata;

import com.google.cloud.firestore.Firestore;
import pt.isel.cn.landmarks.app.domain.LandmarkMetadata;
import pt.isel.cn.landmarks.app.domain.RequestMetadata;

import java.util.List;

import static pt.isel.cn.landmarks.app.Config.FIRESTORE_COLLECTION_NAME;

/**
 * Implementation of the {@link MetadataStorage} interface that uses Firestore.
 */
public class FirestoreMetadataStorage implements MetadataStorage {

    private final Firestore service;

    public FirestoreMetadataStorage(Firestore service) {
        this.service = service;
    }

    @Override
    public void storeRequestMetadata(RequestMetadata requestMetadata) {
        service.collection(FIRESTORE_COLLECTION_NAME)
                .document(requestMetadata.getRequestId())
                .set(requestMetadata);
    }

    @Override
    public void updateLandmarksAndStatus(String requestId, List<LandmarkMetadata> landmarks, String status) {
        service.collection(FIRESTORE_COLLECTION_NAME)
                .document(requestId)
                .update("landmarks", landmarks,
                        "status", status
                );
    }
}
