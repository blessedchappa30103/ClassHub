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
    public ResponseEntity<CourseResponse> createCourse(@RequestParam Long teacherId, @Valid @RequestBody CreateCourseRequest request) {
        CourseResponse response = courseService.createCourse(teacherId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<EnrollmentResponse> joinCourse(@PathVariable Long id, @Valid @RequestBody JoinCourseRequest request) {
        EnrollmentResponse response = courseService.joinCourse(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<EnrollmentResponse> leaveCourse(@PathVariable Long id, @RequestParam Long studentId) {
        EnrollmentResponse response = courseService.leaveCourse(id, studentId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/students/{studentId}")
    public ResponseEntity<EnrollmentResponse> removeStudentFromCourse(@PathVariable Long id, @PathVariable Long studentId, @RequestParam Long teacherId) {
        EnrollmentResponse response = courseService.removeCourse(id, studentId, teacherId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<CourseResponse> archiveCourse(@PathVariable Long id, @RequestParam Long teacherId) {
        return ResponseEntity.ok(courseService.archiveCourse(id, teacherId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id, @RequestParam Long teacherId) {
        courseService.deleteCourse(id, teacherId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/students")
    public ResponseEntity<List<EnrollmentResponse>> getAllActiveStudents(@PathVariable Long id) {
        List<EnrollmentResponse> responses = courseService.getAllActiveUsersInCourse(id);
        return ResponseEntity.ok(responses);
    }
}