package org.example.classhub.DTOs.UserDTOs;

import org.example.classhub.models.Role;
import org.example.classhub.models.User;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String username,
        String email,
        Role role
) {
    public static UserResponse mapToResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
