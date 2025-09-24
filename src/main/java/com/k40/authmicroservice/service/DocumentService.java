package com.k40.authmicroservice.service;

import com.k40.authmicroservice.enums.DocumentReference;
import com.k40.authmicroservice.exception.BadRequestException;
import com.k40.authmicroservice.exception.ResourceNotFoundException;
import com.k40.authmicroservice.repos.DocumentReferenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private DocumentReferenceRepository documentRepository;

    public DocumentReference uploadDocument(MultipartFile file, String documentType,
            String clientId, String userId) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }

            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = UUID.randomUUID() + fileExtension;

            // Save file to filesystem
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Create document reference
            DocumentReference document = new DocumentReference();
            document.setFilename(originalFilename);
            document.setFileType(file.getContentType());
            document.setDocumentType(documentType);
            document.setClientId(clientId);
            document.setUploadedBy(userId);
            document.setStoragePath(filePath.toString());
            document.setUploadedAt(LocalDateTime.now());

            return documentRepository.save(document);

        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
    }

    public List<DocumentReference> getClientDocuments(String clientId) {
        return documentRepository.findByClientId(clientId);
    }

    public List<DocumentReference> getUserDocuments(String userId) {
        return documentRepository.findByUploadedBy(userId);
    }

    public void deleteDocument(String documentId) {
        DocumentReference document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {
            // Delete from filesystem
            Path filePath = Paths.get(document.getStoragePath());
            Files.deleteIfExists(filePath);

            // Delete from database
            documentRepository.delete(document);

        } catch (IOException e) {
            throw new BadRequestException("Failed to delete file: " + e.getMessage());
        }
    }
}
