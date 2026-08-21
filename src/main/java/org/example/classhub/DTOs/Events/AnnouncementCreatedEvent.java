package org.example.classhub.DTOs.Events;

import java.util.List;

public record AnnouncementCreatedEvent(
        Long announcementId,
        Long courseId,
        String courseTitle,
        String announcementContent,
        List<String> studentEmails,
        List<String> attachmentUrls
) {
}
