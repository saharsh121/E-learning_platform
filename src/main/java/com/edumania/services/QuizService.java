package com.edumania.services;

import com.edumania.documents.Quiz;
import com.edumania.documents.Question;
import com.edumania.documents.QuizAttempt;
import com.edumania.repositories.QuizRepository;
import com.edumania.repositories.QuestionRepository;
import com.edumania.repositories.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    // Get all active quizzes
    public List<Quiz> getAllActiveQuizzes() {
        return quizRepository.findByIsActiveTrue();
    }

    // Get quizzes by course ID
    public List<Quiz> getQuizzesByCourseId(String courseId) {
        return quizRepository.findByCourseIdAndIsActiveTrue(courseId);
    }

    // Get quiz by ID
    public Optional<Quiz> getQuizById(String id) {
        return quizRepository.findById(id);
    }

    // Get quiz by quizId (QUIZ-001)
    public Optional<Quiz> getQuizByQuizId(String quizId) {
        return quizRepository.findByQuizId(quizId);
    }

    // Get questions for a quiz
    public List<Question> getQuestionsForQuiz(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // Get quiz with questions
    public Map<String, Object> getQuizWithQuestions(String quizId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
        if (quizOptional.isEmpty()) {
            return null;
        }
        
        Quiz quiz = quizOptional.get();
        List<Question> questions = questionRepository.findByQuizId(quizId);
        
        // Shuffle questions for randomness
        Collections.shuffle(questions);
        
        // Remove correct answers for student view
        List<Map<String, Object>> questionData = new ArrayList<>();
        for (Question q : questions) {
            Map<String, Object> qMap = new HashMap<>();
            qMap.put("id", q.getId());
            qMap.put("questionId", q.getQuestionId());
            qMap.put("questionText", q.getQuestionText());
            qMap.put("options", q.getOptions());
            qMap.put("marks", q.getMarks());
            // Don't send correctOptionIndex to student
            questionData.add(qMap);
        }
        
        result.put("quiz", quiz);
        result.put("questions", questionData);
        
        return result;
    }

    // Submit quiz and calculate score
    public QuizAttempt submitQuiz(String studentId, String quizId, Map<String, Integer> answers) {
        // Get quiz
        Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
        if (quizOptional.isEmpty()) {
            throw new RuntimeException("Quiz not found!");
        }
        
        Quiz quiz = quizOptional.get();
        
        // Get all questions
        List<Question> questions = questionRepository.findByQuizId(quizId);
        
        int totalMarks = 0;
        int obtainedMarks = 0;
        int correct = 0;
        int wrong = 0;
        int unanswered = 0;
        
        List<Map<String, Object>> studentAnswers = new ArrayList<>();
        
        for (Question q : questions) {
            totalMarks += q.getMarks();
            
            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("questionId", q.getQuestionId());
            answerMap.put("questionText", q.getQuestionText());
            answerMap.put("correctOptionIndex", q.getCorrectOptionIndex());
            answerMap.put("marks", q.getMarks());
            answerMap.put("explanation", q.getExplanation());
            
            Integer studentAnswer = answers.get(q.getId());
            if (studentAnswer == null) {
                unanswered++;
                answerMap.put("studentAnswer", -1);
                answerMap.put("isCorrect", false);
            } else {
                answerMap.put("studentAnswer", studentAnswer);
                if (studentAnswer == q.getCorrectOptionIndex()) {
                    correct++;
                    obtainedMarks += q.getMarks();
                    answerMap.put("isCorrect", true);
                } else {
                    wrong++;
                    answerMap.put("isCorrect", false);
                }
            }
            
            studentAnswers.add(answerMap);
        }
        
        // Calculate percentage
        double percentage = totalMarks > 0 ? (double) obtainedMarks / totalMarks * 100 : 0;
        
        // Determine result
        String result = percentage >= quiz.getPassingScore() ? "Passed" : "Failed";
        
        // Create quiz attempt record
        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudentId(studentId);
        attempt.setQuizId(quizId);
        attempt.setCourseId(quiz.getCourseId());
        attempt.setScore(obtainedMarks);
        attempt.setTotalMarks(totalMarks);
        attempt.setPercentage(percentage);
        attempt.setCorrectAnswers(correct);
        attempt.setWrongAnswers(wrong);
        attempt.setUnanswered(unanswered);
        attempt.setStatus("Completed");
        attempt.setResult(result);
        attempt.setStudentAnswers(studentAnswers);
        attempt.setSubmittedAt(LocalDateTime.now());
        
        return quizAttemptRepository.save(attempt);
    }

    // Get student's quiz attempts
    public List<QuizAttempt> getStudentQuizAttempts(String studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    // Get student's quiz attempts for a course
    public List<QuizAttempt> getStudentQuizAttemptsByCourse(String studentId, String courseId) {
        return quizAttemptRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    // Get student's attempt for a specific quiz
    public Optional<QuizAttempt> getStudentQuizAttempt(String studentId, String quizId) {
        return quizAttemptRepository.findByStudentIdAndQuizId(studentId, quizId);
    }

    // Check if student has already attempted a quiz
    public boolean hasAttemptedQuiz(String studentId, String quizId) {
        return quizAttemptRepository.findByStudentIdAndQuizId(studentId, quizId).isPresent();
    }
    // Save quiz
public Quiz saveQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
}

// Update quiz
public Quiz updateQuiz(Quiz quiz) {
    quiz.setUpdatedAt(LocalDateTime.now());
    return quizRepository.save(quiz);
}

// Save question
public Question saveQuestion(Question question) {
    return questionRepository.save(question);
}

// Delete question
public void deleteQuestion(String questionId) {
    questionRepository.deleteById(questionId);
}

// Delete quiz
public void deleteQuiz(String quizId) {
    Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
    quizOptional.ifPresent(quiz -> quizRepository.delete(quiz));
}


    // ==========================================
    // ✅ ADD THIS METHOD - Get quiz attempt by ID
    // ==========================================
    
    public Optional<QuizAttempt> getQuizAttemptById(String attemptId) {
        return quizAttemptRepository.findById(attemptId);
    }
}