package org.example.classhub.DTOs.AnnouncementDTOs;

import org.example.classhub.DTOs.AttachmentDTOs.AttachmentResponse;
import org.example.classhub.models.Announcement;

import java.time.LocalDateTime;
import java.util.List;

public record AnnouncementResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long authorId,
        Long courseId
) {
    public static AnnouncementResponse mapToResponse(Announcement announcement){
        List<AttachmentResponse> attachmentResponse = announcement.getAttachments() != null
                ? announcement.getAttachments().stream().map(AttachmentResponse::mapToResponse).toList()
                : List.of();
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getContent(),
                announcement.getCreatedAt(),
                announcement.getAuthor().getId(),
                announcement.getCourse().getId()
        );
    }
}
