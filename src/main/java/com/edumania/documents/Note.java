package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notes")
public class Note {
    
    @Id
    private String id;
    
    private String courseId;          // Which course this note belongs to
    private String noteId;            // e.g., PHY-101-NOTE-01
    private String title;             // Note title
    private String description;       // Note description
    private String fileUrl;           // URL to download the note (PDF/DOC)
    private String fileType;          // PDF, DOC, PPT, etc.
    private long fileSize;            // File size in bytes
    private String chapter;           // Chapter name
    private String uploadedBy;        // Faculty name
    private String tags;              // Tags for search
    private long downloads;           // Number of downloads
    private LocalDateTime uploadDate;
    private boolean isActive;
    
    // Constructor
    public Note() {
        this.uploadDate = LocalDateTime.now();
        this.isActive = true;
        this.downloads = 0;
    }
    
    public Note(String courseId, String noteId, String title, String fileUrl) {
        this.courseId = courseId;
        this.noteId = noteId;
        this.title = title;
        this.fileUrl = fileUrl;
        this.uploadDate = LocalDateTime.now();
        this.isActive = true;
        this.downloads = 0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getNoteId() { return noteId; }
    public void setNoteId(String noteId) { this.noteId = noteId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public long getDownloads() { return downloads; }
    public void setDownloads(long downloads) { this.downloads = downloads; }
    
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}