package com.edumania.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.edumania.documents.Student;
import com.edumania.documents.Faculty;
import com.edumania.documents.Admin;
import com.edumania.documents.Course;
import com.edumania.documents.Video;
import com.edumania.documents.Note;
import com.edumania.services.StudentService;
import com.edumania.services.FacultyService;
import com.edumania.services.AdminService;
import com.edumania.services.CourseService;
import com.edumania.services.VideoService;
import com.edumania.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Path;
import java.net.MalformedURLException;
import java.io.IOException;

@Controller
public class PageController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private NoteService noteService;

    // ==========================================
    // HOME PAGE
    // ==========================================
    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ==========================================
    // LOGIN PAGES
    // ==========================================
    
    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/faculty-login")
    public String facultyLogin() {
        return "faculty-login";
    }

    @GetMapping("/student-login")
    public String studentLogin() {
        return "student-login";
    }

    // ==========================================
    // STUDENT LOGIN - VERIFY FROM DATABASE
    // ==========================================
    
    @PostMapping("/student/login")
    public String studentLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = studentService.loginStudent(email, password);
            
            if (student != null) {
                session.setAttribute("student", student);
                return "redirect:/student-dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password!");
                return "redirect:/student-login";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/student-login";
        }
    }

    // ==========================================
    // FACULTY LOGIN - VERIFY FROM DATABASE
    // ==========================================
    
    @PostMapping("/faculty/login")
    public String facultyLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Faculty faculty = facultyService.loginFaculty(email, password);
            
            if (faculty != null) {
                session.setAttribute("faculty", faculty);
                return "redirect:/faculty-dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password!");
                return "redirect:/faculty-login";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/faculty-login";
        }
    }

    // ==========================================
    // ADMIN LOGIN - VERIFY FROM DATABASE
    // ==========================================
    
    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Admin admin = adminService.loginAdmin(email, password);
            
            if (admin != null) {
                session.setAttribute("admin", admin);
                return "redirect:/admin-dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password!");
                return "redirect:/admin-login";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/admin-login";
        }
    }

    // ==========================================
    // STUDENT DASHBOARD - WITH COURSES
    // ==========================================
    
    @GetMapping("/student-dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        // Get available and enrolled courses
        List<Course> availableCourses = courseService.getAvailableCourses(student.getId());
        List<Course> enrolledCourses = courseService.getStudentEnrolledCourses(student.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);
        
        return "student-dashboard";
    }

    // ==========================================
    // RESOURCES PAGE - VIEW ALL PDFs
    // ==========================================
    
    @GetMapping("/resources")
    public String viewResources(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        // Get all active notes/resources from database
        List<Note> resources = noteService.getActiveNotes();
        
        model.addAttribute("student", student);
        model.addAttribute("resources", resources);
        
        return "resources";
    }

    // ==========================================
    // ENROLL IN COURSE
    // ==========================================
    
    @PostMapping("/student/enroll")
    public String enrollStudent(
            @RequestParam String courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        try {
            String result = courseService.enrollStudent(student.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/student-dashboard";
    }

    // ==========================================
    // UNENROLL FROM COURSE
    // ==========================================
    
    @PostMapping("/student/unenroll")
    public String unenrollStudent(
            @RequestParam String courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        try {
            String result = courseService.unenrollStudent(student.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/student-dashboard";
    }

    // ==========================================
    // COURSE DETAIL PAGE
    // ==========================================
    
    @GetMapping("/course/{courseId}")
    public String courseDetail(
            @PathVariable String courseId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        Optional<Course> courseOptional = courseService.getCourseByCourseId(courseId);
        
        if (courseOptional.isEmpty()) {
            return "redirect:/student-dashboard";
        }
        
        Course course = courseOptional.get();
        
        if (!courseService.isStudentEnrolled(student.getId(), course.getId())) {
            redirectAttributes.addFlashAttribute("error", "You are not enrolled in this course!");
            return "redirect:/student-dashboard";
        }
        
        // Get videos and notes for this course
        List<Video> videos = videoService.getActiveVideosByCourseId(course.getId());
        List<Note> notes = noteService.getActiveNotesByCourseId(course.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("course", course);
        model.addAttribute("videos", videos);
        model.addAttribute("notes", notes);
        
        return "course-detail";
    }

    // ==========================================
// COURSE PAGES - Physics, Chemistry, Biology
// ==========================================

@GetMapping("/course/physics")
public String physicsCourse(HttpSession session, Model model) {
    Student student = (Student) session.getAttribute("student");
    
    if (student == null) {
        return "redirect:/student-login";
    }
    
    // Get videos directly by courseId (PHY-101)
    List<Video> videos = videoService.getVideosByCourseId("PHY-101");
    System.out.println("✅ Physics Videos found: " + videos.size());
    
    // Debug: Print each video
    for (Video video : videos) {
        System.out.println("📹 Video Title: " + video.getTitle());
        System.out.println("📹 Video URL: " + video.getVideoUrl());
        System.out.println("📹 Course ID: " + video.getCourseId());
    }
    
    model.addAttribute("student", student);
    model.addAttribute("videos", videos);
    
    return "physics";
}

@GetMapping("/course/chemistry")
public String chemistryCourse(HttpSession session, Model model) {
    Student student = (Student) session.getAttribute("student");
    
    if (student == null) {
        return "redirect:/student-login";
    }
    
    // Get videos directly by courseId (CHEM-101)
    List<Video> videos = videoService.getVideosByCourseId("CHEM-101");
    System.out.println("✅ Chemistry Videos found: " + videos.size());
    
    for (Video video : videos) {
        System.out.println("📹 Video Title: " + video.getTitle());
        System.out.println("📹 Video URL: " + video.getVideoUrl());
    }
    
    model.addAttribute("student", student);
    model.addAttribute("videos", videos);
    
    return "chemistry";
}

@GetMapping("/course/biology")
public String biologyCourse(HttpSession session, Model model) {
    Student student = (Student) session.getAttribute("student");
    
    if (student == null) {
        return "redirect:/student-login";
    }
    
    // Get videos directly by courseId (BIO-101)
    List<Video> videos = videoService.getVideosByCourseId("BIO-101");
    System.out.println("✅ Biology Videos found: " + videos.size());
    
    for (Video video : videos) {
        System.out.println("📹 Video Title: " + video.getTitle());
        System.out.println("📹 Video URL: " + video.getVideoUrl());
    }
    
    model.addAttribute("student", student);
    model.addAttribute("videos", videos);
    
    return "biology";
}
    // ==========================================
    // DOWNLOAD NOTE - ACTUAL FILE DOWNLOAD
    // ==========================================

    @GetMapping("/notes/download/{noteId}")
    public ResponseEntity<Resource> downloadNote(
            @PathVariable String noteId,
            HttpSession session) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Optional<Note> noteOptional = noteService.getNoteById(noteId);
            
            if (noteOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Note note = noteOptional.get();
            
            noteService.incrementDownloads(noteId);
            
            String filePath = "src/main/resources/static" + note.getFileUrl();
            File file = new File(filePath);
            
            if (!file.exists()) {
                String fileName = note.getFileUrl().substring(note.getFileUrl().lastIndexOf("/") + 1);
                filePath = "src/main/resources/static/uploads/notes/" + fileName;
                file = new File(filePath);
            }
            
            if (!file.exists()) {
                System.out.println("❌ File not found: " + filePath);
                return ResponseEntity.notFound().build();
            }
            
            Path path = file.toPath();
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + note.getTitle() + ".pdf\"")
                    .body(resource);
            
        } catch (MalformedURLException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
} catch (IOException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
}
    }



    // ==========================================
    // LOGOUT
    // ==========================================
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ==========================================
    // STUDENT REGISTRATION
    // ==========================================
    
    @GetMapping("/student-register")
    public String studentRegister() {
        return "student-register";
    }

    @PostMapping("/student/register")
    public String registerStudent(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String department,
            @RequestParam String year,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setPassword(password);
            student.setDepartment(department);
            student.setYear(year);

            studentService.registerStudent(student);

            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/student-login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/student-register";
        }
    }

    // ==========================================
    // FACULTY REGISTRATION (Admin only)
    // ==========================================
    
    @GetMapping("/faculty-register")
    public String facultyRegister() {
        return "faculty-register";
    }

    @PostMapping("/faculty/register")
    public String registerFaculty(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String department,
            @RequestParam String designation,
            RedirectAttributes redirectAttributes) {

        try {
            Faculty faculty = new Faculty();
            faculty.setFirstName(firstName);
            faculty.setLastName(lastName);
            faculty.setEmail(email);
            faculty.setPassword(password);
            faculty.setDepartment(department);
            faculty.setDesignation(designation);

            facultyService.registerFaculty(faculty);

            redirectAttributes.addFlashAttribute("success", "Faculty added successfully!");
            return "redirect:/faculty-login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed: " + e.getMessage());
            return "redirect:/faculty-register";
        }
    }

    // ==========================================
    // ADMIN REGISTRATION (Super Admin only)
    // ==========================================
    
    @GetMapping("/admin-register")
    public String adminRegister() {
        return "admin-register";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) boolean isSuperAdmin,
            RedirectAttributes redirectAttributes) {

        try {
            Admin admin = new Admin();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setRole(role);
            admin.setSuperAdmin(isSuperAdmin);

            adminService.registerAdmin(admin);

            redirectAttributes.addFlashAttribute("success", "Admin added successfully!");
            return "redirect:/admin-login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed: " + e.getMessage());
            return "redirect:/admin-register";
        }
    }
}
