package org.example.classhub.DTOs.CourseDTOs;

import jakarta.validation.constraints.NotBlank;

public record CreateCourseRequest(
        @NotBlank String title
) {
}
