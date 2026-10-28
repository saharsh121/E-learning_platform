package com.edumania.controller;

import com.edumania.documents.Admin;
import com.edumania.documents.Faculty;
import com.edumania.documents.Student;
import com.edumania.services.AdminService;
import com.edumania.services.FacultyService;
import com.edumania.services.StudentService;
import com.edumania.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================
    
    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        // Get all data
        List<Student> students = studentService.getAllStudents();
        List<Faculty> faculty = facultyService.getAllFaculty();
        List<Admin> admins = adminService.getAllAdmins();
        
        model.addAttribute("admin", admin);
        model.addAttribute("students", students);
        model.addAttribute("faculty", faculty);
        model.addAttribute("admins", admins);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalFaculty", faculty.size());
        model.addAttribute("totalAdmins", admins.size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        
        return "admin-dashboard";
    }

    // ==========================================
    // ADD STUDENT
    // ==========================================
    
    @PostMapping("/admin/add-student")
    public String addStudent(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String department,
            @RequestParam String year,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        try {
            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setPassword(password);
            student.setDepartment(department);
            student.setYear(year);
            student.setActive(true);
            
            studentService.registerStudent(student);
            
            redirectAttributes.addFlashAttribute("success", "Student added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add student: " + e.getMessage());
        }
        
        return "redirect:/admin-dashboard";
    }

    // ==========================================
    // ADD FACULTY
    // ==========================================
    
    @PostMapping("/admin/add-faculty")
    public String addFaculty(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String department,
            @RequestParam String designation,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        try {
            Faculty faculty = new Faculty();
            faculty.setFirstName(firstName);
            faculty.setLastName(lastName);
            faculty.setEmail(email);
            faculty.setPassword(password);
            faculty.setDepartment(department);
            faculty.setDesignation(designation);
            faculty.setActive(true);
            
            facultyService.registerFaculty(faculty);
            
            redirectAttributes.addFlashAttribute("success", "Faculty added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add faculty: " + e.getMessage());
        }
        
        return "redirect:/admin-dashboard";
    }

    // ==========================================
    // SEARCH STUDENT
    // ==========================================
    
    @GetMapping("/admin/search-student")
    public String searchStudent(
            @RequestParam String keyword,
            HttpSession session,
            Model model) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        // Get all students
        List<Student> students = studentService.getAllStudents();
        List<Faculty> faculty = facultyService.getAllFaculty();
        List<Admin> admins = adminService.getAllAdmins();
        
        // Search for student
        Student foundStudent = null;
        String searchError = null;
        
        for (Student student : students) {
            String fullName = (student.getFirstName() + " " + student.getLastName()).toLowerCase();
            if (fullName.contains(keyword.toLowerCase()) || 
                student.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                foundStudent = student;
                break;
            }
        }
        
        if (foundStudent == null) {
            searchError = "No student found with keyword: " + keyword;
        }
        
        model.addAttribute("admin", admin);
        model.addAttribute("students", students);
        model.addAttribute("faculty", faculty);
        model.addAttribute("admins", admins);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalFaculty", faculty.size());
        model.addAttribute("totalAdmins", admins.size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("searchedStudent", foundStudent);
        model.addAttribute("searchError", searchError);
        
        return "admin-dashboard";
    }

    // ==========================================
    // SEARCH FACULTY
    // ==========================================
    
    @GetMapping("/admin/search-faculty")
    public String searchFaculty(
            @RequestParam String keyword,
            HttpSession session,
            Model model) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        // Get all data
        List<Student> students = studentService.getAllStudents();
        List<Faculty> faculty = facultyService.getAllFaculty();
        List<Admin> admins = adminService.getAllAdmins();
        
        // Search for faculty
        Faculty foundFaculty = null;
        String facultySearchError = null;
        
        for (Faculty facultyMember : faculty) {
            String fullName = (facultyMember.getFirstName() + " " + facultyMember.getLastName()).toLowerCase();
            if (fullName.contains(keyword.toLowerCase()) || 
                facultyMember.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                foundFaculty = facultyMember;
                break;
            }
        }
        
        if (foundFaculty == null) {
            facultySearchError = "No faculty found with keyword: " + keyword;
        }
        
        model.addAttribute("admin", admin);
        model.addAttribute("students", students);
        model.addAttribute("faculty", faculty);
        model.addAttribute("admins", admins);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalFaculty", faculty.size());
        model.addAttribute("totalAdmins", admins.size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("searchedFaculty", foundFaculty);
        model.addAttribute("facultySearchError", facultySearchError);
        
        return "admin-dashboard";
    }

    // ==========================================
    // DELETE STUDENT
    // ==========================================
    
    @PostMapping("/admin/delete-student")
    public String deleteStudent(
            @RequestParam String studentId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        try {
            studentService.deleteStudent(studentId);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete student: " + e.getMessage());
        }
        
        return "redirect:/admin-dashboard";
    }

    // ==========================================
    // DELETE FACULTY
    // ==========================================
    
    @PostMapping("/admin/delete-faculty")
    public String deleteFaculty(
            @RequestParam String facultyId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin-login";
        }
        
        try {
            facultyService.deleteFaculty(facultyId);
            redirectAttributes.addFlashAttribute("success", "Faculty deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete faculty: " + e.getMessage());
        }
        
        return "redirect:/admin-dashboard";
    }
}