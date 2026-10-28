package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "quiz_attempts")
public class QuizAttempt {
    
    @Id
    private String id;
    
    private String studentId;         // Student who took the quiz
    private String quizId;            // Quiz ID
    private String courseId;          // Course ID
    private int score;               // Total marks obtained
    private int totalMarks;          // Total possible marks
    private double percentage;       // Score percentage
    private int correctAnswers;       // Number of correct answers
    private int wrongAnswers;         // Number of wrong answers
    private int unanswered;           // Number of unanswered questions
    private String status;           // Not Started, In Progress, Completed
    private String result;           // Passed, Failed
    private int timeTaken;           // Time taken in seconds
    private List<Map<String, Object>> studentAnswers; // Student's answers
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime submittedAt;
    
    // Constructors
    public QuizAttempt() {}
    
    public QuizAttempt(String studentId, String quizId, String courseId) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.courseId = courseId;
        this.status = "Not Started";
        this.startTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
    
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public int getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(int wrongAnswers) { this.wrongAnswers = wrongAnswers; }
    
    public int getUnanswered() { return unanswered; }
    public void setUnanswered(int unanswered) { this.unanswered = unanswered; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    
    public int getTimeTaken() { return timeTaken; }
    public void setTimeTaken(int timeTaken) { this.timeTaken = timeTaken; }
    
    public List<Map<String, Object>> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<Map<String, Object>> studentAnswers) { this.studentAnswers = studentAnswers; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}