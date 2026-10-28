package com.edumania.repositories;

import com.edumania.documents.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    
    
    Optional<Student> findByEmail(String email);
    
    
    List<Student> findByDepartment(String department);
    
    
    List<Student> findByIsActiveTrue();
}