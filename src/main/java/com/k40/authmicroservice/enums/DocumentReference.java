package com.k40.authmicroservice.enums;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "document_references")
public class DocumentReference {
    @Id
    private String id;

    private String filename;
    private String fileType;
    private String documentType; // e.g., "KRA_CERTIFICATE", "ID_PASSPORT", "PROFILE_PHOTO"
    private LocalDateTime uploadedAt = LocalDateTime.now();
    private String storagePath; // Path to stored file

    private String clientId;
    private String uploadedBy;
}
