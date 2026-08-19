package org.example.classhub.DTOs.CourseDTOs;

import jakarta.validation.constraints.NotBlank;

public record JoinCourseRequest(
        @NotBlank String joinCode
) { }
