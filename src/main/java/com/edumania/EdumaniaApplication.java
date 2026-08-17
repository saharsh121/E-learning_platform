package com.edumania;

import com.edumania.documents.Student;
import com.edumania.documents.Faculty;
import com.edumania.documents.Admin;
import com.edumania.services.StudentService;
import com.edumania.services.FacultyService;
import com.edumania.services.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EdumaniaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdumaniaApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            StudentService studentService,
            FacultyService facultyService,
            AdminService adminService) {
        return args -> {
            
            System.out.println("📚 Initializing MongoDB with test data...");
            
            // 1. Create a Test Student
            if (studentService.getStudentByEmail("student@edumania.com").isEmpty()) {
                Student student = new Student("John", "Doe", "student@edumania.com", "password123");
                student.setDepartment("Computer Science");
                student.setYear("3rd Year");
                studentService.registerStudent(student);
                System.out.println("✅ Student added: john.doe@edumania.com");
            }
            
            // 2. Create a Test Faculty
            if (facultyService.getFacultyByEmail("faculty@edumania.com").isEmpty()) {
                Faculty faculty = new Faculty("Dr. Jane", "Smith", "faculty@edumania.com", "password123");
                faculty.setDepartment("Computer Science");
                faculty.setDesignation("Professor");
                facultyService.registerFaculty(faculty);
                System.out.println("✅ Faculty added: faculty@edumania.com");
            }
            
            // 3. Create a Test Admin
            if (adminService.getAdminByEmail("admin@edumania.com").isEmpty()) {
                Admin admin = new Admin("Super", "Admin", "admin@edumania.com", "admin123");
                admin.setRole("Super Admin");
                admin.setSuperAdmin(true);
                adminService.registerAdmin(admin);
                System.out.println("✅ Admin added: admin@edumania.com");
            }
            
            System.out.println("🎉 Database initialization complete!");
            System.out.println("📊 Total Students: " + studentService.getAllStudents().size());
            System.out.println("📊 Total Faculty: " + facultyService.getAllFaculty().size());
            System.out.println("📊 Total Admins: " + adminService.getAllAdmins().size());
        };
    }
}