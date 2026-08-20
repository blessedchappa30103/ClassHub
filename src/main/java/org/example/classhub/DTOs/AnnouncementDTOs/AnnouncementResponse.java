package org.example.classhub.DTOs.AnnouncementDTOs;

import org.example.classhub.models.Announcement;

import java.time.LocalDateTime;

public record AnnouncementResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long authorId,
        Long courseId
) {
    public static AnnouncementResponse mapToResponse(Announcement announcment){
        return new AnnouncementResponse(
                announcment.getId(),
                announcment.getContent(),
                announcment.getCreatedAt(),
                announcment.getAuthor().getId(),
                announcment.getCourse().getId()
        );
    }
}
