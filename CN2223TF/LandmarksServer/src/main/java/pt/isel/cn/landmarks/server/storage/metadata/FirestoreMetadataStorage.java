package pt.isel.cn.landmarks.server.storage.metadata;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import pt.isel.cn.landmarks.server.domain.RequestMetadata;

import java.util.Optional;

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
    public Optional<RequestMetadata> getRequestMetadata(String requestId) {
        try {
            CollectionReference colRef = service.collection(COLLECTION_NAME);
            ApiFuture<QuerySnapshot> query = colRef.whereEqualTo("requestId", requestId).get();

            Optional<QueryDocumentSnapshot> documentSnapshot = query.get().getDocuments().stream().findFirst();

            return documentSnapshot.map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(RequestMetadata.class));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

//    @Override
//    public List<Request> getRequestsMetadataByConfidence(float confidenceThreshold) {
//        try {
//            CollectionReference colRef = service.collection(COLLECTION_NAME);
//            ApiFuture<QuerySnapshot> query = colRef.whereGreaterThanOrEqualTo("confidence", confidenceThreshold).get();
//
//            return query.get().getDocuments().stream()
//                    .map(doc -> doc.toObject(Request.class))
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            System.out.println("Error getting documents: " + e.getMessage());
//            return Collections.emptyList();
//        }
//    }
}
