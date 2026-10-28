package com.edumania.services;

import com.edumania.documents.Faculty;
import com.edumania.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    
    @Autowired
    private FacultyRepository facultyRepository;
    
    
    public Faculty registerFaculty(Faculty faculty) {
        if (facultyRepository.findByEmail(faculty.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        faculty.setJoinDate(LocalDateTime.now());
        faculty.setActive(true);
        return facultyRepository.save(faculty);
    }
    
    
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
    
    
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    
    
    public Optional<Faculty> getFacultyById(@NonNull String id) {
        return facultyRepository.findById(id);
    }
    
    
    public Optional<Faculty> getFacultyByEmail(String email) {
        return facultyRepository.findByEmail(email);
    }
    
    
    public Faculty updateFaculty(@NonNull Faculty faculty) {
        return facultyRepository.save(faculty);
    }
    
    
    public void deleteFaculty(@NonNull String id) {
        facultyRepository.deleteById(id);
    }
}