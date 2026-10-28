package com.edumania.repositories;

import com.edumania.documents.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    
    List<Quiz> findByCourseId(String courseId);
    
    List<Quiz> findByIsActiveTrue();
    
    List<Quiz> findByCourseIdAndIsActiveTrue(String courseId);
    
    Optional<Quiz> findByQuizId(String quizId);  // ✅ ADDED THIS METHOD
}