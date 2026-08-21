package org.example.classhub.DTOs.AttachmentDTOs;

import org.example.classhub.models.Attachment;

public record AttachmentResponse(
        Long id,
        String fileName,
        String downloadUrl
) {
    public static AttachmentResponse mapToResponse(Attachment attachment) {
        return new AttachmentResponse(
                attachment.getId(),
                attachment.getOriginalFileName(),
                attachment.getDownloadUrl()
        );
    }
}
