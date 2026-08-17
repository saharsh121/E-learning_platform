package com.edumania.repositories;

import com.edumania.documents.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    
    Optional<Course> findByCourseId(String courseId);
    
    List<Course> findByStatus(String status);
    
    List<Course> findByDepartment(String department);
    
    List<Course> findByEnrolledStudentsContaining(String studentId);
}