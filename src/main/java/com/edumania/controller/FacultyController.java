package com.edumania.controller;

import com.edumania.documents.Faculty;
import com.edumania.documents.Video;
import com.edumania.documents.Note;
import com.edumania.services.VideoService;
import com.edumania.services.NoteService;
import com.edumania.services.CloudinaryBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class FacultyController {

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    @Autowired
    private VideoService videoService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CloudinaryBookService cloudinaryBookService;

    
    
    
    
    @GetMapping("/faculty-dashboard")
    public String facultyDashboard(HttpSession session, Model model) {
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        
        List<Video> lectures = videoService.getAllVideos();
        List<Note> notes = noteService.getAllNotes();
        model.addAttribute("books", List.of());
        try {
            model.addAttribute("books", cloudinaryBookService.getBooks());
        } catch (Exception e) {
            logger.error("Unable to load books from Cloudinary", e);
            model.addAttribute("booksError", "Books could not be loaded. Check the Cloudinary configuration and try again.");
        }
        
        model.addAttribute("faculty", faculty);
        model.addAttribute("lectures", lectures);
        model.addAttribute("notes", notes);
        
        return "faculty-dashboard";
    }

    @PostMapping("/faculty/upload-book")
    public String uploadBook(
            @RequestParam String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        if (faculty == null) {
            return "redirect:/faculty-login";
        }

        String normalizedTitle = title.trim();
        String normalizedDescription = description.trim();
        if (normalizedTitle.isEmpty() || normalizedTitle.length() > 150) {
            redirectAttributes.addFlashAttribute("error", "Book title is required and must be 150 characters or fewer.");
            return "redirect:/faculty-dashboard";
        }
        if (normalizedDescription.length() > 1000) {
            redirectAttributes.addFlashAttribute("error", "Book description must be 1000 characters or fewer.");
            return "redirect:/faculty-dashboard";
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            redirectAttributes.addFlashAttribute("error", "Only PDF files are allowed.");
            return "redirect:/faculty-dashboard";
        }
        if (file.getSize() > 10L * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("error", "Book PDF files must be 10 MB or smaller.");
            return "redirect:/faculty-dashboard";
        }

        String facultyName = faculty.getFirstName() + " " + faculty.getLastName();
        try {
            cloudinaryBookService.uploadBook(file, normalizedTitle, normalizedDescription, facultyName);
            redirectAttributes.addFlashAttribute("success", "Book uploaded successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            logger.error("Unable to upload book to Cloudinary", e);
            redirectAttributes.addFlashAttribute("error", "Book upload failed. Please check Cloudinary and try again.");
        }

        return "redirect:/faculty-dashboard";
    }

    
    
    
    
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

    
    
    
    
    @PostMapping("/faculty/delete-lecture")
    public String deleteLecture(
            @RequestParam @NonNull String lectureId,
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
            
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a PDF file!");
                return "redirect:/faculty-dashboard";
            }
            
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                redirectAttributes.addFlashAttribute("error", "Only PDF files are allowed!");
                return "redirect:/faculty-dashboard";
            }
            
            
            String filePath = "/uploads/notes/" + fileName;
            String fullPath = "src/main/resources/static" + filePath;
            
            
            java.io.File targetFile = new java.io.File(fullPath);
            targetFile.getParentFile().mkdirs();
            file.transferTo(targetFile);
            
            
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

    
    
    
    
    @PostMapping("/faculty/delete-note")
    public String deleteNote(
            @RequestParam @NonNull String noteId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Faculty faculty = (Faculty) session.getAttribute("faculty");
        
        if (faculty == null) {
            return "redirect:/faculty-login";
        }
        
        try {
            
            Note note = noteService.getNoteById(noteId).orElse(null);
            if (note != null) {
                
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