package com.edumania.repositories;

import com.edumania.documents.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    
    List<Enrollment> findByStudentId(String studentId);
    
    List<Enrollment> findByCourseId(String courseId);
    
    Optional<Enrollment> findByStudentIdAndCourseId(String studentId, String courseId);
    
    List<Enrollment> findByStudentIdAndStatus(String studentId, String status);
}