package com.edumania.controller;

import com.edumania.documents.Faculty;
import com.edumania.documents.Video;
import com.edumania.documents.Note;
import com.edumania.services.FacultyService;
import com.edumania.services.VideoService;
import com.edumania.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private NoteService noteService;

    // ==========================================
    // FACULTY DASHBOARD
    // ==========================================
    
    @GetMapping("/faculty-dashboard")
    public String facultyDashboard(HttpSession session, Model model) {
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        // Get all lectures and notes
        List<Video> lectures = videoService.getAllVideos();
        List<Note> notes = noteService.getAllNotes();
        
        model.addAttribute("faculty", faculty);
        model.addAttribute("lectures", lectures);
        model.addAttribute("notes", notes);
        
        return "faculty-dashboard";
    }

    // ==========================================
    // UPLOAD LECTURE
    // ==========================================
    
    @PostMapping("/faculty/upload-lecture")
    public String uploadLecture(
            @RequestParam String courseId,
            @RequestParam String title,
            @RequestParam String videoUrl,
            @RequestParam String chapter,
            @RequestParam int duration,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        try {
            // Create new video/lecture
            Video video = new Video();
            video.setCourseId(courseId);
            video.setVideoId(courseId + "-VID-" + UUID.randomUUID().toString().substring(0, 6));
            video.setTitle(title);
            video.setVideoUrl(videoUrl);
            video.setChapter(chapter);
            video.setDuration(duration);
            video.setInstructor(faculty.getFirstName() + " " + faculty.getLastName());
            video.setUploadDate(LocalDateTime.now());
            video.setActive(true);
            video.setViews(0);
            
            videoService.createVideo(video);
            
            redirectAttributes.addFlashAttribute("success", "Lecture uploaded successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload lecture: " + e.getMessage());
        }
        
        return "redirect:/faculty-dashboard";
    }

    // ==========================================
    // DELETE LECTURE
    // ==========================================
    
    @PostMapping("/faculty/delete-lecture")
    public String deleteLecture(
            @RequestParam String lectureId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        try {
            videoService.deleteVideo(lectureId);
            redirectAttributes.addFlashAttribute("success", "Lecture deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete lecture: " + e.getMessage());
        }
        
        return "redirect:/faculty-dashboard";
    }

    // ==========================================
    // UPLOAD NOTE (PDF)
    // ==========================================
    
    @PostMapping("/faculty/upload-note")
    public String uploadNote(
            @RequestParam String courseId,
            @RequestParam String title,
            @RequestParam String chapter,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a PDF file!");
                return "redirect:/faculty-dashboard";
            }
            
            // Check if file is PDF
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                redirectAttributes.addFlashAttribute("error", "Only PDF files are allowed!");
                return "redirect:/faculty-dashboard";
            }
            
            // Save file to static/uploads/notes/
            String filePath = "/uploads/notes/" + fileName;
            String fullPath = "src/main/resources/static" + filePath;
            
            // Create directories if not exist
            java.io.File targetFile = new java.io.File(fullPath);
            targetFile.getParentFile().mkdirs();
            file.transferTo(targetFile);
            
            // Create note in database
            Note note = new Note();
            note.setCourseId(courseId);
            note.setNoteId(courseId + "-NOTE-" + UUID.randomUUID().toString().substring(0, 6));
            note.setTitle(title);
            note.setChapter(chapter);
            note.setFileUrl(filePath);
            note.setFileType("PDF");
            note.setFileSize(file.getSize());
            note.setUploadedBy(faculty.getFirstName() + " " + faculty.getLastName());
            note.setUploadDate(LocalDateTime.now());
            note.setActive(true);
            note.setDownloads(0);
            
            noteService.createNote(note);
            
            redirectAttributes.addFlashAttribute("success", "Note uploaded successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload note: " + e.getMessage());
        }
        
        return "redirect:/faculty-dashboard";
    }

    // ==========================================
    // DELETE NOTE
    // ==========================================
    
    @PostMapping("/faculty/delete-note")
    public String deleteNote(
            @RequestParam String noteId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        try {
            // Get note to get file path
            Note note = noteService.getNoteById(noteId).orElse(null);
            if (note != null) {
                // Delete file from system
                String filePath = "src/main/resources/static" + note.getFileUrl();
                java.io.File file = new java.io.File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("success", "Note deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete note: " + e.getMessage());
        }
        
        return "redirect:/faculty-dashboard";
    }
}