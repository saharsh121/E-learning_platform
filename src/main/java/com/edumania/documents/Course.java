package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "courses")
public class Course {
    
    @Id
    private String id;
    
    private String courseId;          // e.g., PHY-101, CHEM-101, BIO-101
    private String courseName;        // e.g., Physics, Chemistry, Biology
    private String description;       // Course description
    private String department;        // e.g., Science
    private String instructor;        // Faculty name
    private int credits;              // Credit hours
    private int duration;             // Duration in weeks
    private String thumbnail;         // Course thumbnail image URL
    private String status;            // Active, Inactive, Draft
    private List<String> enrolledStudents;  // List of student IDs
    private int maxStudents;          // Maximum enrollment capacity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor
    public Course() {
        this.createdAt = LocalDateTime.now();
        this.status = "Active";
    }
    
    public Course(String courseId, String courseName, String description, String instructor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.createdAt = LocalDateTime.now();
        this.status = "Active";
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<String> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(List<String> enrolledStudents) { this.enrolledStudents = enrolledStudents; }
    
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}