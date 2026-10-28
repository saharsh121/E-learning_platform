package com.edumania.services;

import com.edumania.documents.Course;
import com.edumania.documents.Enrollment;
import com.edumania.repositories.CourseRepository;
import com.edumania.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    
    public List<Course> getAllCourses() {
        return courseRepository.findByStatus("Active");
    }

    
    public Optional<Course> getCourseById(@NonNull String id) {
        return courseRepository.findById(id);
    }

    
    public Optional<Course> getCourseByCourseId(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    
    public List<Course> getStudentEnrolledCourses(String studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        List<Course> enrolledCourses = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            String courseId = java.util.Objects.requireNonNull(
                    enrollment.getCourseId(), "Enrollment course ID must not be null");
            Optional<Course> course = courseRepository.findById(courseId);
            course.ifPresent(enrolledCourses::add);
        }
        
        return enrolledCourses;
    }

    
    public List<Course> getAvailableCourses(String studentId) {
        List<Course> allCourses = getAllCourses();
        List<Course> enrolledCourses = getStudentEnrolledCourses(studentId);
        List<Course> availableCourses = new ArrayList<>();
        
        
        List<String> enrolledCourseIds = new ArrayList<>();
        for (Course course : enrolledCourses) {
            enrolledCourseIds.add(course.getId());
        }
        
        
        for (Course course : allCourses) {
            if (!enrolledCourseIds.contains(course.getId())) {
                availableCourses.add(course);
            }
        }
        
        return availableCourses;
    }

    
    public Course createCourse(Course course) {
        if (courseRepository.findByCourseId(course.getCourseId()).isPresent()) {
            throw new RuntimeException("Course ID already exists!");
        }
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setEnrolledStudents(new ArrayList<>());
        return courseRepository.save(course);
    }

    
    public Course updateCourse(Course course) {
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    
    public void deleteCourse(@NonNull String id) {
        courseRepository.deleteById(id);
    }

    
    public String enrollStudent(String studentId, @NonNull String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found!");
        }
        
        Course course = courseOptional.get();
        
        
        if (course.getEnrolledStudents() != null && course.getEnrolledStudents().contains(studentId)) {
            throw new RuntimeException("Student already enrolled in this course!");
        }
        
        
        if (course.getEnrolledStudents() != null && course.getEnrolledStudents().size() >= course.getMaxStudents()) {
            throw new RuntimeException("Course is full!");
        }
        
        
        if (course.getEnrolledStudents() == null) {
            course.setEnrolledStudents(new ArrayList<>());
        }
        course.getEnrolledStudents().add(studentId);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
        
        
        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollmentRepository.save(enrollment);
        
        return "Student enrolled successfully!";
    }

    
    public String unenrollStudent(String studentId, @NonNull String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found!");
        }
        
        Course course = courseOptional.get();
        
        
        if (course.getEnrolledStudents() != null) {
            course.getEnrolledStudents().remove(studentId);
            course.setUpdatedAt(LocalDateTime.now());
            courseRepository.save(course);
        }
        
        
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        enrollmentOptional.ifPresent(enrollmentRepository::delete);
        
        return "Student unenrolled successfully!";
    }

    
    public boolean isStudentEnrolled(String studentId, String courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent();
    }
}