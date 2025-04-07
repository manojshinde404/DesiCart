package com.shinde.desicart.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;

@Service
public class FileStorageService {

    private Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            // Resolve relative to project root in development
            this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

            // Create directory if not exists
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);

                // Set permissions (Linux/Mac only)
                try {
                    Files.setPosixFilePermissions(
                            fileStorageLocation,
                            PosixFilePermissions.fromString("rwxr-x---")
                    );
                } catch (UnsupportedOperationException e) {
                    // Windows will throw this - permissions not critical for dev
                }

                System.out.println("Created upload directory at: " + fileStorageLocation);
            }
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not initialize upload directory: " + uploadDir, ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Security check
        if (fileName.contains("..")) {
            throw new IOException("Filename contains invalid path sequence: " + fileName);
        }

        // Generate unique filename
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

        // Copy file to target location
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }
}