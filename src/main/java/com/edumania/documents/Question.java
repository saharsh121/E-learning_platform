package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "questions")
public class Question {
    
    @Id
    private String id;
    
    private String questionId;        // e.g., Q-001
    private String quizId;            // Which quiz this belongs to
    private String questionText;      // The actual question
    private List<String> options;     // A, B, C, D options
    private int correctOptionIndex;   // Index of correct option (0, 1, 2, 3)
    private int marks;               // Marks for this question
    private String explanation;       // Explanation for the answer
    private String difficulty;        // Easy, Medium, Hard
    private String category;          // MCQ, True/False
    
    // Constructors
    public Question() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }
    
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
    
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
    
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}