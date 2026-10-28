package com.edumania.services;

import com.edumania.documents.Course;
import com.edumania.documents.Enrollment;
import com.edumania.repositories.CourseRepository;
import com.edumania.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Get all active courses
    public List<Course> getAllCourses() {
        return courseRepository.findByStatus("Active");
    }

    // Get course by ID
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    // Get course by courseId (e.g., PHY-101)
    public Optional<Course> getCourseByCourseId(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    // Get courses by department
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    // Get courses a student is enrolled in
    public List<Course> getStudentEnrolledCourses(String studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        List<Course> enrolledCourses = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            Optional<Course> course = courseRepository.findById(enrollment.getCourseId());
            course.ifPresent(enrolledCourses::add);
        }
        
        return enrolledCourses;
    }

    // Get available courses (not enrolled in)
    public List<Course> getAvailableCourses(String studentId) {
        List<Course> allCourses = getAllCourses();
        List<Course> enrolledCourses = getStudentEnrolledCourses(studentId);
        List<Course> availableCourses = new ArrayList<>();
        
        // Get enrolled course IDs
        List<String> enrolledCourseIds = new ArrayList<>();
        for (Course course : enrolledCourses) {
            enrolledCourseIds.add(course.getId());
        }
        
        // Filter out enrolled courses
        for (Course course : allCourses) {
            if (!enrolledCourseIds.contains(course.getId())) {
                availableCourses.add(course);
            }
        }
        
        return availableCourses;
    }

    // Create new course
    public Course createCourse(Course course) {
        if (courseRepository.findByCourseId(course.getCourseId()).isPresent()) {
            throw new RuntimeException("Course ID already exists!");
        }
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setEnrolledStudents(new ArrayList<>());
        return courseRepository.save(course);
    }

    // Update course
    public Course updateCourse(Course course) {
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    // Delete course
    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    // Enroll student in course
    public String enrollStudent(String studentId, String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found!");
        }
        
        Course course = courseOptional.get();
        
        // Check if student already enrolled
        if (course.getEnrolledStudents() != null && course.getEnrolledStudents().contains(studentId)) {
            throw new RuntimeException("Student already enrolled in this course!");
        }
        
        // Check max capacity
        if (course.getEnrolledStudents() != null && course.getEnrolledStudents().size() >= course.getMaxStudents()) {
            throw new RuntimeException("Course is full!");
        }
        
        // Add student to course
        if (course.getEnrolledStudents() == null) {
            course.setEnrolledStudents(new ArrayList<>());
        }
        course.getEnrolledStudents().add(studentId);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
        
        // Create enrollment record
        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollmentRepository.save(enrollment);
        
        return "Student enrolled successfully!";
    }

    // Unenroll student from course
    public String unenrollStudent(String studentId, String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found!");
        }
        
        Course course = courseOptional.get();
        
        // Remove student from course
        if (course.getEnrolledStudents() != null) {
            course.getEnrolledStudents().remove(studentId);
            course.setUpdatedAt(LocalDateTime.now());
            courseRepository.save(course);
        }
        
        // Delete enrollment record
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        enrollmentOptional.ifPresent(enrollmentRepository::delete);
        
        return "Student unenrolled successfully!";
    }

    // Check if student is enrolled
    public boolean isStudentEnrolled(String studentId, String courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent();
    }
}