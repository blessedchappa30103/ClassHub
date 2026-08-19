package org.example.classhub.services;

import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.CourseDTOs.CourseResponse;
import org.example.classhub.DTOs.CourseDTOs.CreateCourseRequest;
import org.example.classhub.DTOs.CourseDTOs.JoinCourseRequest;
import org.example.classhub.DTOs.EnrollmentResponse;
import org.example.classhub.models.*;
import org.example.classhub.repository.CourseRepository;
import org.example.classhub.repository.EnrollmentRepository;
import org.example.classhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public CourseResponse createCourse(Long teacherID, CreateCourseRequest request){
        User teacher = userRepository.findById(teacherID).orElseThrow(()->new IllegalArgumentException("Пользователь не найден"));
        if(teacher.getRole()!= Role.TEACHER) throw new IllegalArgumentException("Пользователь не является учителем");

        Course course = new Course();
        course.setTitle(request.title());
        course.setJoinCode(UUID.randomUUID().toString().substring(0,6).toUpperCase());
        Course savedCourse = courseRepository.save(course);
        return CourseResponse.mapToResponse(savedCourse);
    }
    @Transactional
    public EnrollmentResponse joinCourse(Long id, JoinCourseRequest request){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Пользователь не найден"));
        Course course = courseRepository.findByJoinCode(request.joinCode()).orElseThrow(()->new IllegalArgumentException("Курс уже существует"));
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(user);
        enrollment.setCourse(course);
        enrollment.setStatus(Status.ACTIVE);

        user.getEnrollments().add(enrollment);
        return EnrollmentResponse.mapToResponse(enrollment);
    }

    @Transactional
    public EnrollmentResponse removeCourse (Long courseId, Long studentId, Long teacherId){
        User initiator = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Инициатор не найден"));

        if (initiator.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Только преподаватель может удалять студентов");
        }
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new IllegalArgumentException("Запись о зачислении студента не найдена"));

        enrollment.setStatus(Status.DROPPED);

        return EnrollmentResponse.mapToResponse(enrollment);
    }
    @Transactional
    public EnrollmentResponse leaveCourse (Long courseId, Long studentId){
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        enrollment.setStatus(Status.DROPPED);
        return EnrollmentResponse.mapToResponse(enrollment);
    }
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Курс не найден"));
        return CourseResponse.mapToResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses(){
        return courseRepository.findAll().stream().map(CourseResponse::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllActiveUsersInCourse(Long courseId){
        if (!courseRepository.existsById(courseId)) throw new IllegalArgumentException("Курс с ID " + courseId + " не найден");

        return enrollmentRepository.findAllByCourseIdAndStatus(courseId, Status.ACTIVE)
                .stream()
                .map(EnrollmentResponse::mapToResponse)
                .toList();
    }
}
