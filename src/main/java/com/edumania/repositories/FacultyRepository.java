package com.edumania.repositories;

import com.edumania.documents.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface FacultyRepository extends MongoRepository<Faculty, String> {
    
    
    Optional<Faculty> findByEmail(String email);
    
    
    List<Faculty> findByDepartment(String department);
    
    
    List<Faculty> findByIsActiveTrue();
}