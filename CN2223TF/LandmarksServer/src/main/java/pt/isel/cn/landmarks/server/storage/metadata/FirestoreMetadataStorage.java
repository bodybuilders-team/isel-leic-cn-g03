package pt.isel.cn.landmarks.server.storage.metadata;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import pt.isel.cn.landmarks.server.domain.RequestMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pt.isel.cn.landmarks.server.Config.FIRESTORE_COLLECTION_NAME;

/**
 * Implementation of the {@link MetadataStorage} interface that uses Firestore.
 */
public class FirestoreMetadataStorage implements MetadataStorage {

    private final Firestore service;

    public FirestoreMetadataStorage(Firestore service) {
        this.service = service;
    }


    @Override
    public Optional<RequestMetadata> getRequestMetadata(String requestId) {
        try {
            CollectionReference colRef = service.collection(FIRESTORE_COLLECTION_NAME);
            ApiFuture<QuerySnapshot> query = colRef.whereEqualTo("requestId", requestId).get();

            Optional<QueryDocumentSnapshot> documentSnapshot = query.get().getDocuments().stream().findFirst();

            return documentSnapshot.map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(RequestMetadata.class));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<RequestMetadata> getRequestMetadataByConfidence(float confidenceThreshold) {
        try {
            CollectionReference colRef = service.collection(FIRESTORE_COLLECTION_NAME);
            ApiFuture<QuerySnapshot> query = colRef.whereGreaterThanOrEqualTo(
                    "landmarks.confidence", confidenceThreshold).get();

            return query.get().getDocuments().stream()
                    .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(RequestMetadata.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
