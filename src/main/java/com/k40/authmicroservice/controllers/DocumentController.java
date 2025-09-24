package com.k40.authmicroservice.controllers;

import com.k40.authmicroservice.enums.DocumentReference;
import com.k40.authmicroservice.service.DocumentService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentReference> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "clientId", required = false) String clientId,
            Authentication authentication) {

        String userId = authentication.getName();
        DocumentReference doc = documentService.uploadDocument(
                file, documentType, clientId, userId);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DocumentReference>> getClientDocuments(
            @PathVariable String clientId) {
        return ResponseEntity.ok(documentService.getClientDocuments(clientId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<DocumentReference>> getUserDocuments(
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(documentService.getUserDocuments(userId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
