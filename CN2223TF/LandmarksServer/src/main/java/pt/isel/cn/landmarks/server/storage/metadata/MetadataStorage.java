package pt.isel.cn.landmarks.server.storage.metadata;

import pt.isel.cn.landmarks.server.domain.RequestMetadata;

import java.util.List;
import java.util.Optional;

/**
 * Interface for handling metadata related to the requests and landmarks.
 */
public interface MetadataStorage {

    /**
     * Gets the request metadata identified by the given id.
     *
     * @param requestId the id of the request
     * @return the request
     */
    Optional<RequestMetadata> getRequestMetadata(String requestId);

    /**
     * Gets all request metadata.
     *
     * @return the list of request metadata
     */
    List<RequestMetadata> getAllRequestMetadata();
}
