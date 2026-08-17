package com.edumania.services;

import com.edumania.documents.Student;
import com.edumania.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Register new student
    public Student registerStudent(Student student) {
        // Check if email already exists
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        student.setEnrollmentDate(LocalDateTime.now());
        student.setActive(true);
        return studentRepository.save(student);
    }
    
    // Login student
    public Student loginStudent(String email, String password) {
    Optional<Student> studentOptional = studentRepository.findByEmail(email);
    
    if (studentOptional.isPresent()) {
        Student student = studentOptional.get();
        if (student.getPassword().equals(password)) {
            // Update last login time
            student.setLastLogin(LocalDateTime.now());
            studentRepository.save(student);
            return student;  // Return Student object, not Optional
        }
    }
    return null;  // Return null if login fails
}
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    // Get student by ID
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }
    
    // Get student by email
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    // Update student
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }
    
    // Delete student
    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }
    
    // Get active students
    public List<Student> getActiveStudents() {
        return studentRepository.findByIsActiveTrue();
    }
}