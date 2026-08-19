package org.example.classhub.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.CourseDTOs.CourseResponse;
import org.example.classhub.DTOs.UserDTOs.CreateUserRequest;
import org.example.classhub.DTOs.UserDTOs.UserResponse;
import org.example.classhub.models.Course;
import org.example.classhub.models.Role;
import org.example.classhub.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@RequestParam Long id){
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
       List<UserResponse> response = userService.getAllUsers();
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/alluser")
    public ResponseEntity<List<CourseResponse>> getAllUserCourse(@RequestParam Long id){
        List<CourseResponse> response = userService.getAllUserCourses(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam @Valid String email){
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
