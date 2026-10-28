package com.edumania.services;

import com.edumania.documents.Video;
import com.edumania.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    
    public List<Video> getActiveVideos() {
        return videoRepository.findByIsActiveTrue();
    }

    
    public Optional<Video> getVideoById(@NonNull String id) {
        return videoRepository.findById(id);
    }

    
    public List<Video> getVideosByCourseId(String courseId) {
        return videoRepository.findByCourseId(courseId);
    }

    
    public List<Video> getActiveVideosByCourseId(String courseId) {
        return videoRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    
    public Video createVideo(Video video) {
        video.setUploadDate(LocalDateTime.now());
        video.setViews(0);
        video.setActive(true);
        return videoRepository.save(video);
    }

    
    public Video updateVideo(@NonNull Video video) {
        return videoRepository.save(video);
    }

    
    public void deleteVideo(@NonNull String id) {
        videoRepository.deleteById(id);
    }

    
    public void deactivateVideo(@NonNull String id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setActive(false);
            videoRepository.save(video);
        }
    }

    
    public void incrementViews(@NonNull String id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setViews(video.getViews() + 1);
            videoRepository.save(video);
        }
    }

    
    public long getVideoCountByCourseId(String courseId) {
        return videoRepository.findByCourseId(courseId).size();
    }
}