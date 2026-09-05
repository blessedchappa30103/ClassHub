package org.example.classhub.services;

import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.CourseDTOs.CourseResponse;
import org.example.classhub.DTOs.UserDTOs.CreateUserRequest;
import org.example.classhub.DTOs.UserDTOs.UserResponse;
import org.example.classhub.models.Course;
import org.example.classhub.models.Enrollment;
import org.example.classhub.models.User;
import org.example.classhub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.classhub.DTOs.UserDTOs.UserResponse.mapToResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser (CreateUserRequest request){
        if (userRepository.existsByEmail(request.email())) throw new IllegalArgumentException("Пользователь уже существует");
        User user = new User();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя не найден"));
        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException(""));
        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream().map(UserResponse::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Пользователь не найден"));
        return mapToResponse(user);
    }
    @Transactional
    public UserResponse deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Пользователь не найден"));
        UserResponse response = mapToResponse(user);
        userRepository.delete(user);
        return response;
    }
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllUserCourses(Long id){
        User student = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));

        return student.getEnrollments().stream()
                .map(Enrollment::getCourse)
                .map(CourseResponse::mapToResponse) // Преобразуем Entity -> DTO
                .toList();
    }
}
