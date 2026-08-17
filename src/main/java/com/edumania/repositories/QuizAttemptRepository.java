package com.edumania.repositories;

import com.edumania.documents.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
    
    List<QuizAttempt> findByStudentId(String studentId);
    
    List<QuizAttempt> findByStudentIdAndCourseId(String studentId, String courseId);
    
    List<QuizAttempt> findByQuizId(String quizId);
    
    Optional<QuizAttempt> findByStudentIdAndQuizId(String studentId, String quizId);
    
    List<QuizAttempt> findByStudentIdAndStatus(String studentId, String status);
    
    // ✅ This method already exists (inherited from MongoRepository)
    // Optional<QuizAttempt> findById(String id);
}