package org.example.classhub.DTOs.AnnouncementDTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateAnnouncementRequest<attachmentIds>(
        @NotBlank(message = "Текст объявления не может быть пустым")
        String content,
        List<Long> attachmentIds
) { }
