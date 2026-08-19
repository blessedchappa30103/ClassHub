package org.example.classhub.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.CourseDTOs.CourseResponse;
import org.example.classhub.DTOs.CourseDTOs.CreateCourseRequest;
import org.example.classhub.DTOs.CourseDTOs.JoinCourseRequest;
import org.example.classhub.DTOs.EnrollmentResponse;
import org.example.classhub.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestParam Long id, @Valid @RequestBody CreateCourseRequest request){
        CourseResponse response = courseService.createCourse(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@RequestParam Long id){
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses(){
         List<CourseResponse> responses = courseService.getAllCourses();
         return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
    @PostMapping("/join")
    public ResponseEntity<EnrollmentResponse> joinCourse(@RequestParam Long studentId, @RequestBody JoinCourseRequest request){
        EnrollmentResponse response = courseService.joinCourse(studentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping
    public ResponseEntity<EnrollmentResponse> leaveCourse(@RequestParam Long courseId, @RequestParam Long studentId){
        EnrollmentResponse response = courseService.leaveCourse(courseId, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> removeCourse(@RequestParam Long courseId, @RequestParam Long studentId, @RequestParam Long teacherId){
        EnrollmentResponse response = courseService.removeCourse(courseId, studentId, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EnrollmentResponse>> getAllActiveStudent(@RequestParam Long courseId){
        List<EnrollmentResponse> responses = courseService.getAllActiveUsersInCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
