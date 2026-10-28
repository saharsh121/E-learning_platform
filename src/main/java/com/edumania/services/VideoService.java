package com.edumania.services;

import com.edumania.documents.Video;
import com.edumania.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    // Get all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // Get active videos only
    public List<Video> getActiveVideos() {
        return videoRepository.findByIsActiveTrue();
    }

    // Get video by ID
    public Optional<Video> getVideoById(String id) {
        return videoRepository.findById(id);
    }

    // Get videos by course ID
    public List<Video> getVideosByCourseId(String courseId) {
        return videoRepository.findByCourseId(courseId);
    }

    // Get active videos by course ID (ordered)
    public List<Video> getActiveVideosByCourseId(String courseId) {
        return videoRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    // Create new video
    public Video createVideo(Video video) {
        video.setUploadDate(LocalDateTime.now());
        video.setViews(0);
        video.setActive(true);
        return videoRepository.save(video);
    }

    // Update video
    public Video updateVideo(Video video) {
        return videoRepository.save(video);
    }

    // Delete video
    public void deleteVideo(String id) {
        videoRepository.deleteById(id);
    }

    // Soft delete (deactivate)
    public void deactivateVideo(String id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setActive(false);
            videoRepository.save(video);
        }
    }

    // Increment view count
    public void incrementViews(String id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setViews(video.getViews() + 1);
            videoRepository.save(video);
        }
    }

    // Get video count by course
    public long getVideoCountByCourseId(String courseId) {
        return videoRepository.findByCourseId(courseId).size();
    }
}