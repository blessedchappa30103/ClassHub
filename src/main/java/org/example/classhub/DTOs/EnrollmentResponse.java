package org.example.classhub.DTOs;

import org.example.classhub.models.Enrollment;
import org.example.classhub.models.Status;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentFullName,
        Long courseId,
        String courseTitle,
        Status status
) {
    public static EnrollmentResponse fromEntity(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getName() + " " + enrollment.getStudent().getSurname(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getStatus()
        );
    }
}