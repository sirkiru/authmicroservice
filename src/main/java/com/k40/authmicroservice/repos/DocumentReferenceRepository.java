package com.k40.authmicroservice.repos;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.k40.authmicroservice.enums.DocumentReference;

public interface DocumentReferenceRepository extends MongoRepository<DocumentReference, String> {
    List<DocumentReference> findByClientId(String clientId);

    List<DocumentReference> findByUploadedBy(String userId);
}
