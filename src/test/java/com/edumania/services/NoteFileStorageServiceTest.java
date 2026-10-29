package com.edumania.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

class NoteFileStorageServiceTest {

    @TempDir
    Path uploadDirectory;

    private NoteFileStorageService storageService() {
        return new NoteFileStorageService(uploadDirectory.toString());
    }

    @Test
    void storesPdfWithUniqueNameAndLoadsIt() throws IOException {
        NoteFileStorageService storage = storageService();
        byte[] pdf = "%PDF-1.7\nsample content".getBytes();
        MockMultipartFile firstUpload = new MockMultipartFile("file", "notes.pdf", "application/pdf", pdf);
        MockMultipartFile secondUpload = new MockMultipartFile("file", "notes.pdf", "application/pdf", pdf);

        String firstUrl = storage.store(firstUpload);
        String secondUrl = storage.store(secondUpload);
        Resource storedFile = storage.load(firstUrl);

        assertNotEquals(firstUrl, secondUrl);
        assertTrue(storedFile.exists());
        assertArrayEquals(pdf, Files.readAllBytes(Path.of(storedFile.getFile().toURI())));
    }

    @Test
    void rejectsNonPdfContentEvenWhenFileHasPdfExtension() {
        MockMultipartFile upload = new MockMultipartFile(
                "file", "not-a-pdf.pdf", "application/pdf", "plain text".getBytes());

        assertThrows(IllegalArgumentException.class, () -> storageService().store(upload));
    }

    @Test
    void rejectsPathsOutsideTheNotesDirectory() {
        NoteFileStorageService storage = storageService();

        assertThrows(IllegalArgumentException.class, () -> storage.load("/uploads/notes/../secret.pdf"));
        assertThrows(IllegalArgumentException.class, () -> storage.delete("/uploads/notes/../secret.pdf"));
    }
}
