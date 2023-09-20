package com.sdk.authservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sdk.authservice.entity.DocumentEntity;
import com.sdk.authservice.entity.UserEntity;
import com.sdk.authservice.repository.AuthRepo;
import com.sdk.authservice.repository.DocumentRepo;
import com.sdk.authservice.service.FileStorageService;
import com.sdk.authservice.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth-service/kyc")
public class DocumentController {

    @Autowired
    private AuthRepo userRepository;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    JwtUtil jwtUtil;

    // Assuming you've a service to handle file storage (like AWS S3)
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload", name = "UPLOAD KYC DOCUMENT")
    public ResponseEntity<?> uploadKYCDocument(
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        UUID id = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            id = jwtUtil.extractUserId(token);
        }
        // Fetch the user
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Store the file and get its URL
        String documentUrl = fileStorageService.storeFile(id, file);

        // Save the document reference in the DB
        DocumentEntity doc = new DocumentEntity();
        doc.setUser(user);
        doc.setDocumentType(documentType);
        doc.setDocumentUrl(documentUrl);
        documentRepo.save(doc);

        return ResponseEntity.ok("Document uploaded successfully");
    }
}
