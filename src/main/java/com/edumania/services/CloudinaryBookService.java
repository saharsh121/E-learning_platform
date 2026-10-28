package com.edumania.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.edumania.documents.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryBookService {

    private static final String BOOKS_PREFIX = "edumania/books/";
    private static final String PDF_SUFFIX = ".pdf";

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    public Book uploadBook(MultipartFile file, String title, String description, String uploadedBy)
            throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a PDF file.");
        }

        byte[] content = file.getBytes();
        if (content.length < 5
                || content[0] != '%'
                || content[1] != 'P'
                || content[2] != 'D'
                || content[3] != 'F'
                || content[4] != '-') {
            throw new IllegalArgumentException("The selected file is not a valid PDF.");
        }

        String publicId = BOOKS_PREFIX + UUID.randomUUID() + ".pdf";
        String uploadedAt = Instant.now().toString();
        Map<?, ?> result = cloudinary().uploader().upload(content, ObjectUtils.asMap(
                "resource_type", "raw",
                "public_id", publicId,
                "access_mode", "public",
                "overwrite", false,
                "context", ObjectUtils.asMap(
                        "title", title,
                        "description", description,
                        "uploaded_by", uploadedBy,
                        "uploaded_at", uploadedAt)));

        return new Book(
                publicId,
                title,
                description,
                (String) result.get("secure_url"),
                uploadedBy,
                uploadedAt);
    }

    public List<Book> getBooks() throws Exception {
        Cloudinary cloudinary = cloudinary();
        List<Book> books = new ArrayList<>();
        String nextCursor = null;

        do {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("resource_type", "raw");
            parameters.put("type", "upload");
            parameters.put("prefix", BOOKS_PREFIX);
            parameters.put("context", true);
            parameters.put("max_results", 500);
            if (nextCursor != null) {
                parameters.put("next_cursor", nextCursor);
            }

            Map<?, ?> response = cloudinary.api().resources(parameters);
            Object resources = response.get("resources");
            if (resources instanceof List<?> resourceList) {
                for (Object resource : resourceList) {
                    if (resource instanceof Map<?, ?> resourceMap) {
                        books.add(toBook(resourceMap, cloudinary));
                    }
                }
            }
            nextCursor = (String) response.get("next_cursor");
        } while (nextCursor != null && !nextCursor.isBlank());

        return books;
    }

    public BookDownload getBookDownload(String publicId) throws Exception {
        String uuid = publicId.startsWith(BOOKS_PREFIX)
                && publicId.endsWith(PDF_SUFFIX)
                ? publicId.substring(BOOKS_PREFIX.length(), publicId.length() - PDF_SUFFIX.length())
                : "";
        if (uuid.isEmpty() || !UUID.fromString(uuid).toString().equalsIgnoreCase(uuid)) {
            throw new IllegalArgumentException("Invalid book identifier.");
        }

        Map<?, ?> resource = cloudinary().api().resource(
                publicId,
                ObjectUtils.asMap("resource_type", "raw", "context", true));
        String downloadUrl = stringValue(resource.get("secure_url"));
        if (downloadUrl.isBlank()) {
            throw new IllegalStateException("Cloudinary did not return a download URL for the book.");
        }

        Map<?, ?> context = asMap(resource.get("context"));
        Map<?, ?> customContext = asMap(context.get("custom"));
        if (customContext.isEmpty()) {
            customContext = context;
        }
        String title = valueOrDefault(customContext.get("title"), "book");
        String filename = title.replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "_").trim();
        if (filename.isBlank()) {
            filename = "book";
        }

        return new BookDownload(downloadUrl, filename + PDF_SUFFIX);
    }

    private Cloudinary cloudinary() {
        if (isBlank(cloudName) || isBlank(apiKey) || isBlank(apiSecret)) {
            throw new IllegalStateException(
                    "Cloudinary is not configured. Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET.");
        }

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    private Book toBook(Map<?, ?> resource, Cloudinary cloudinary) {
        Map<?, ?> context = asMap(resource.get("context"));
        Map<?, ?> customContext = asMap(context.get("custom"));
        if (customContext.isEmpty()) {
            customContext = context;
        }

        String publicId = stringValue(resource.get("public_id"));
        String uploadedAt = stringValue(customContext.get("uploaded_at"));
        if (uploadedAt.isBlank()) {
            uploadedAt = stringValue(resource.get("created_at"));
        }

        return new Book(
                publicId,
                valueOrDefault(customContext.get("title"), "Untitled book"),
                stringValue(customContext.get("description")),
                "/books/download?publicId=" + URLEncoder.encode(publicId, StandardCharsets.UTF_8),
                stringValue(customContext.get("uploaded_by")),
                uploadedAt);
    }

    private Map<?, ?> asMap(Object value) {
        return value instanceof Map<?, ?> map ? map : Map.of();
    }

    private String valueOrDefault(Object value, String fallback) {
        String text = stringValue(value);
        return text.isBlank() ? fallback : text;
    }

    private String stringValue(Object value) {
        return value instanceof String text ? text : "";
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record BookDownload(String url, String filename) {
    }
}
