package com.sdk.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Assuming the uploads directory is in the project root
    private final String storageLocation = "uploads";

    public String storeFile(UUID userId, MultipartFile file) {
        // Verify if the file type is valid
        String fileType = file.getContentType();
        if (!fileType.equals("application/pdf") && !fileType.startsWith("image/")) {
            throw new RuntimeException("Invalid file type. Only PDF and images are allowed.");
        }

        try {
            Path storageDirectory = Paths.get(storageLocation).toAbsolutePath().normalize();

            // Ensure the storage directory exists or create it if it doesn't
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
            }

            // Define the target directory: user_id_current_timestamp
            String fileName =+ System.currentTimeMillis() + "-" + file.getOriginalFilename();

            String uniqueDirName = userId.toString();
            Path userDirectory = storageDirectory.resolve(uniqueDirName);
            Files.createDirectories(userDirectory); // Create the unique user directory

            Path targetLocation = userDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();

        } catch (IOException ex) {
            throw new RuntimeException("Error occurred while storing the file.", ex);
        }
    }

    // You can add more methods if necessary, like delete, list files, etc.
}

