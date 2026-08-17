package com.edumania.repositories;

import com.edumania.documents.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    
    List<Question> findByQuizId(String quizId);
    
    List<Question> findByQuizIdAndDifficulty(String quizId, String difficulty);
}