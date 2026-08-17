package com.edumania.repositories;

import com.edumania.documents.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    
    List<Note> findByCourseId(String courseId);
    
    List<Note> findByCourseIdAndIsActiveTrue(String courseId);
    
    List<Note> findByUploadedBy(String uploadedBy);
    List<Note> findByIsActiveTrue();
}