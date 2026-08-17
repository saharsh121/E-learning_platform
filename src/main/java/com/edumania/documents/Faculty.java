package com.edumania.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "faculty")
public class Faculty {
    
    @Id
    private String id;
    
    private String facultyId;      // Custom faculty ID like FAC-2024-001
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String department;
    private String designation;     // Professor, Associate Professor, Assistant Professor
    private String specialization;
    private List<String> coursesTeaching;
    private double experience;      // Years of experience
    private LocalDateTime joinDate;
    private LocalDateTime lastLogin;
    private boolean isActive;
    
    // Constructors
    public Faculty() {}
    
    public Faculty(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.joinDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public List<String> getCoursesTeaching() { return coursesTeaching; }
    public void setCoursesTeaching(List<String> coursesTeaching) { this.coursesTeaching = coursesTeaching; }
    
    public double getExperience() { return experience; }
    public void setExperience(double experience) { this.experience = experience; }
    
    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}