package org.example.classhub.DTOs;

import jakarta.validation.constraints.NotNull;
import org.example.classhub.models.Status;

public record UpdateEnrollmentStatusRequest(
        @NotNull Status status
) {
}
