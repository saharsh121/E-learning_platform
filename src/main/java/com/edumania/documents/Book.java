package com.edumania.documents;

public record Book(
        String publicId,
        String title,
        String description,
        String fileUrl,
        String uploadedBy,
        String uploadedAt) {
}
