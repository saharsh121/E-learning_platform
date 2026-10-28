package com.edumania.repositories;

import com.edumania.documents.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    
    
    Optional<Admin> findByEmail(String email);
    
    
    Optional<Admin> findByIsSuperAdminTrue();
}