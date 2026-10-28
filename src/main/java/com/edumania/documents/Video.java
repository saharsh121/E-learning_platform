package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "videos")
public class Video {
    
    @Id
    private String id;
    
    private String courseId;          // Which course this video belongs to
    private String videoId;           // e.g., PHY-101-VID-01
    private String title;             // Video title
    private String description;       // Video description
    private String videoUrl;          // YouTube URL or hosted video URL
    private String thumbnailUrl;      // Video thumbnail
    private int duration;             // Duration in minutes
    private int orderIndex;           // Order of video in the course
    private String chapter;           // Chapter name
    private String instructor;        // Who uploaded
    private long views;               // Number of views
    private LocalDateTime uploadDate;
    private boolean isActive;
    
    // Constructor
    public Video() {
        this.uploadDate = LocalDateTime.now();
        this.isActive = true;
        this.views = 0;
    }
    
    public Video(String courseId, String videoId, String title, String videoUrl) {
        this.courseId = courseId;
        this.videoId = videoId;
        this.title = title;
        this.videoUrl = videoUrl;
        this.uploadDate = LocalDateTime.now();
        this.isActive = true;
        this.views = 0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    
    public long getViews() { return views; }
    public void setViews(long views) { this.views = views; }
    
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}