package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "quizzes")
public class Quiz {
    
    @Id
    private String id;
    
    private String quizId;           // e.g., QUIZ-001
    private String courseId;         // PHY-101, CHEM-101, BIO-101
    private String title;            // "Physics Quiz 1"
    private String description;      // "Test your knowledge on Mechanics"
    private int duration;            // Duration in minutes
    private int totalQuestions;      // Total number of questions
    private int passingScore;        // Minimum score to pass (percentage)
    private String difficulty;       // Easy, Medium, Hard
    private boolean isActive;
    private LocalDateTime startDate; // Quiz available from
    private LocalDateTime endDate;   // Quiz available until
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> questionIds; // List of question IDs
    
    // Constructors
    public Quiz() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public int getPassingScore() { return passingScore; }
    public void setPassingScore(int passingScore) { this.passingScore = passingScore; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<String> getQuestionIds() { return questionIds; }
    public void setQuestionIds(List<String> questionIds) { this.questionIds = questionIds; }
}