package com.edumania.services;

import com.edumania.documents.Faculty;
import com.edumania.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    
    @Autowired
    private FacultyRepository facultyRepository;
    
    // Register new faculty
    public Faculty registerFaculty(Faculty faculty) {
        if (facultyRepository.findByEmail(faculty.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        faculty.setJoinDate(LocalDateTime.now());
        faculty.setActive(true);
        return facultyRepository.save(faculty);
    }
    
    // Login faculty
    public Faculty loginFaculty(String email, String password) {
    Optional<Faculty> facultyOptional = facultyRepository.findByEmail(email);
    
    if (facultyOptional.isPresent()) {
        Faculty faculty = facultyOptional.get();
        if (faculty.getPassword().equals(password)) {
            faculty.setLastLogin(LocalDateTime.now());
            facultyRepository.save(faculty);
            return faculty;
        }
    }
    return null;
}
    
    // Get all faculty
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    
    // Get faculty by ID
    public Optional<Faculty> getFacultyById(String id) {
        return facultyRepository.findById(id);
    }
    
    // Get faculty by email
    public Optional<Faculty> getFacultyByEmail(String email) {
        return facultyRepository.findByEmail(email);
    }
    
    // Update faculty
    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
    
    // Delete faculty
    public void deleteFaculty(String id) {
        facultyRepository.deleteById(id);
    }
}