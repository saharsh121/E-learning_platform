package com.edumania.repositories;

import com.edumania.documents.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface FacultyRepository extends MongoRepository<Faculty, String> {
    
    // Find faculty by email (for login)
    Optional<Faculty> findByEmail(String email);
    
    // Find faculty by department
    List<Faculty> findByDepartment(String department);
    
    // Find active faculty
    List<Faculty> findByIsActiveTrue();
}