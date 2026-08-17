package com.edumania.repositories;

import com.edumania.documents.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    
    List<Video> findByCourseId(String courseId);
    
    List<Video> findByCourseIdOrderByOrderIndexAsc(String courseId);
    
    List<Video> findByIsActiveTrue();
}