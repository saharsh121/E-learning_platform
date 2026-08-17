package com.edumania.repositories;

import com.edumania.documents.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    
    // Find student by email (for login)
    Optional<Student> findByEmail(String email);
    
    // Find students by department
    List<Student> findByDepartment(String department);
    
    // Find active students
    List<Student> findByIsActiveTrue();
}