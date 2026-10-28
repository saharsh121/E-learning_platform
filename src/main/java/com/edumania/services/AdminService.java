package com.edumania.services;

import com.edumania.documents.Admin;
import com.edumania.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    
    public Admin registerAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        admin.setJoinDate(LocalDateTime.now());
        admin.setActive(true);
        return adminRepository.save(admin);
    }
    
    
    public Admin loginAdmin(String email, String password) {
    Optional<Admin> adminOptional = adminRepository.findByEmail(email);
    
    if (adminOptional.isPresent()) {
        Admin admin = adminOptional.get();
        if (admin.getPassword().equals(password)) {
            admin.setLastLogin(LocalDateTime.now());
            adminRepository.save(admin);
            return admin;
        }
    }
    return null;
}
    
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    
    public Optional<Admin> getAdminById(@NonNull String id) {
        return adminRepository.findById(id);
    }
    
    
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
    
    public Admin updateAdmin(@NonNull Admin admin) {
        return adminRepository.save(admin);
    }
    
    
    public void deleteAdmin(@NonNull String id) {
        adminRepository.deleteById(id);
    }
}