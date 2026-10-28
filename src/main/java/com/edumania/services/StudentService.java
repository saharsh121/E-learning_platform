package com.edumania.services;

import com.edumania.documents.Student;
import com.edumania.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    
    public Student registerStudent(Student student) {
        
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        student.setEnrollmentDate(LocalDateTime.now());
        student.setActive(true);
        return studentRepository.save(student);
    }
    
    
    public Student loginStudent(String email, String password) {
    Optional<Student> studentOptional = studentRepository.findByEmail(email);
    
    if (studentOptional.isPresent()) {
        Student student = studentOptional.get();
        if (student.getPassword().equals(password)) {
            
            student.setLastLogin(LocalDateTime.now());
            studentRepository.save(student);
            return student;  
        }
    }
    return null;  
}
    
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    
    public Optional<Student> getStudentById(@NonNull String id) {
        return studentRepository.findById(id);
    }
    
    
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    
    public Student updateStudent(@NonNull Student student) {
        return studentRepository.save(student);
    }
    
    
    public void deleteStudent(@NonNull String id) {
        studentRepository.deleteById(id);
    }
    
    
    public List<Student> getActiveStudents() {
        return studentRepository.findByIsActiveTrue();
    }
}