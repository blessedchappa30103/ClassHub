package org.example.classhub.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.AnnouncementDTOs.AnnouncementResponse;
import org.example.classhub.DTOs.AnnouncementDTOs.CreateAnnouncementRequest;
import org.example.classhub.services.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping()
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @PathVariable Long courseId,
            @RequestParam Long authorId,
            @Valid @RequestBody CreateAnnouncementRequest request) {

        AnnouncementResponse response = announcementService.createAnnouncement(courseId, authorId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncements(@PathVariable Long courseId) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByCourse(courseId));
    }
}
