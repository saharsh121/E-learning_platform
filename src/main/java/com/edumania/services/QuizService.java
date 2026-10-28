package com.edumania.services;

import com.edumania.documents.Quiz;
import com.edumania.documents.Question;
import com.edumania.documents.QuizAttempt;
import com.edumania.repositories.QuizRepository;
import com.edumania.repositories.QuestionRepository;
import com.edumania.repositories.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    
    public List<Quiz> getAllActiveQuizzes() {
        return quizRepository.findByIsActiveTrue();
    }

    
    public List<Quiz> getQuizzesByCourseId(String courseId) {
        return quizRepository.findByCourseIdAndIsActiveTrue(courseId);
    }

    
    public Optional<Quiz> getQuizById(@NonNull String id) {
        return quizRepository.findById(id);
    }

    
    public Optional<Quiz> getQuizByQuizId(String quizId) {
        return quizRepository.findByQuizId(quizId);
    }

    
    public List<Question> getQuestionsForQuiz(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    
    public Map<String, Object> getQuizWithQuestions(String quizId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
        if (quizOptional.isEmpty()) {
            return null;
        }
        
        Quiz quiz = quizOptional.get();
        List<Question> questions = questionRepository.findByQuizId(quizId);
        
        
        Collections.shuffle(questions);
        
        
        List<Map<String, Object>> questionData = new ArrayList<>();
        for (Question q : questions) {
            Map<String, Object> qMap = new HashMap<>();
            qMap.put("id", q.getId());
            qMap.put("questionId", q.getQuestionId());
            qMap.put("questionText", q.getQuestionText());
            qMap.put("options", q.getOptions());
            qMap.put("marks", q.getMarks());
            
            questionData.add(qMap);
        }
        
        result.put("quiz", quiz);
        result.put("questions", questionData);
        
        return result;
    }

    
    public QuizAttempt submitQuiz(String studentId, String quizId, Map<String, Integer> answers) {
        
        Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
        if (quizOptional.isEmpty()) {
            throw new RuntimeException("Quiz not found!");
        }
        
        Quiz quiz = quizOptional.get();
        
        
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
        
        
        double percentage = totalMarks > 0 ? (double) obtainedMarks / totalMarks * 100 : 0;
        
        
        String result = percentage >= quiz.getPassingScore() ? "Passed" : "Failed";
        
        
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

    
    public List<QuizAttempt> getStudentQuizAttempts(String studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    
    public List<QuizAttempt> getStudentQuizAttemptsByCourse(String studentId, String courseId) {
        return quizAttemptRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    
    public Optional<QuizAttempt> getStudentQuizAttempt(String studentId, String quizId) {
        return quizAttemptRepository.findByStudentIdAndQuizId(studentId, quizId);
    }

    
    public boolean hasAttemptedQuiz(String studentId, String quizId) {
        return quizAttemptRepository.findByStudentIdAndQuizId(studentId, quizId).isPresent();
    }
    
public Quiz saveQuiz(@NonNull Quiz quiz) {
    return quizRepository.save(quiz);
}


public Quiz updateQuiz(@NonNull Quiz quiz) {
    quiz.setUpdatedAt(LocalDateTime.now());
    return quizRepository.save(quiz);
}


public Question saveQuestion(@NonNull Question question) {
    return questionRepository.save(question);
}


public void deleteQuestion(@NonNull String questionId) {
    questionRepository.deleteById(questionId);
}


public void deleteQuiz(String quizId) {
    Optional<Quiz> quizOptional = quizRepository.findByQuizId(quizId);
    quizOptional.ifPresent(quiz -> quizRepository.delete(
            java.util.Objects.requireNonNull(quiz, "Quiz repository returned a null quiz")));
}


    
    
    
    
    public Optional<QuizAttempt> getQuizAttemptById(@NonNull String attemptId) {
        return quizAttemptRepository.findById(attemptId);
    }
}