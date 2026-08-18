package org.example.classhub.DTOs.UserDTOs;

import org.example.classhub.models.Role;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        Role role
) {}
