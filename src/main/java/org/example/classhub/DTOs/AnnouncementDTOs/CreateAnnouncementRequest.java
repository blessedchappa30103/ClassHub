package org.example.classhub.DTOs.AnnouncementDTOs;

import jakarta.validation.constraints.NotBlank;

public record CreateAnnouncementRequest(
        @NotBlank(message = "Текст объявления не может быть пустым")
        String content
) { }
