package com.edumania.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoteFileStorageService {

    private static final String FILE_URL_PREFIX = "/uploads/notes/";

    private final Path uploadDirectory;

    public NoteFileStorageService(@Value("${edumania.upload-dir:uploads/notes}") String uploadDirectory) {
        this.uploadDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a PDF file.");
        }
        if (!hasPdfSignature(file)) {
            throw new IllegalArgumentException("The selected file is not a valid PDF.");
        }

        Files.createDirectories(uploadDirectory);
        String fileName = UUID.randomUUID() + ".pdf";
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, uploadDirectory.resolve(fileName));
        }
        return FILE_URL_PREFIX + fileName;
    }

    public Resource load(String fileUrl) throws IOException {
        if (fileUrl == null || !fileUrl.startsWith(FILE_URL_PREFIX)) {
            throw new IllegalArgumentException("Invalid note file path.");
        }

        String fileName = fileUrl.substring(FILE_URL_PREFIX.length());
        if (fileName.isBlank() || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid note file path.");
        }

        Path storedFile = uploadDirectory.resolve(fileName).normalize();
        if (!storedFile.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid note file path.");
        }
        if (Files.exists(storedFile)) {
            return new UrlResource(storedFile.toUri());
        }

        Resource legacyFile = new ClassPathResource("static" + fileUrl);
        if (legacyFile.exists()) {
            return legacyFile;
        }
        return new UrlResource(storedFile.toUri());
    }

    public void delete(String fileUrl) throws IOException {
        if (fileUrl == null || !fileUrl.startsWith(FILE_URL_PREFIX)) {
            throw new IllegalArgumentException("Invalid note file path.");
        }

        String fileName = fileUrl.substring(FILE_URL_PREFIX.length());
        if (fileName.isBlank() || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid note file path.");
        }

        Path storedFile = uploadDirectory.resolve(fileName).normalize();
        if (!storedFile.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid note file path.");
        }
        Files.deleteIfExists(storedFile);
    }

    public String getResourceLocation() {
        String location = uploadDirectory.toUri().toString();
        return location.endsWith("/") ? location : location + "/";
    }

    private boolean hasPdfSignature(MultipartFile file) throws IOException {
        try (InputStream input = file.getInputStream()) {
            byte[] header = input.readNBytes(1024);
            byte[] signature = {'%', 'P', 'D', 'F', '-'};
            for (int i = 0; i <= header.length - signature.length; i++) {
                boolean matches = true;
                for (int j = 0; j < signature.length; j++) {
                    if (header[i + j] != signature[j]) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return true;
                }
            }
            return false;
        }
    }
}
