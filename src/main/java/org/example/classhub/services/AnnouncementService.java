package org.example.classhub.services;

import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.AnnouncementDTOs.AnnouncementResponse;
import org.example.classhub.DTOs.AnnouncementDTOs.CreateAnnouncementRequest;
import org.example.classhub.DTOs.Events.AnnouncementCreatedEvent;
import org.example.classhub.models.Announcement;
import org.example.classhub.models.Course;
import org.example.classhub.models.User;
import org.example.classhub.repository.AnnouncementRepository;
import org.example.classhub.repository.CourseRepository;
import org.example.classhub.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, AnnouncementCreatedEvent> kafkaTemplate;
    @Transactional
    public AnnouncementResponse createAnnouncement(Long courseId, Long authorId, CreateAnnouncementRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден: " + courseId));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + authorId));

        Announcement announcement = new Announcement();
        announcement.setContent(request.content());
        announcement.setCourse(course);
        announcement.setAuthor(author);

        Announcement saved = announcementRepository.save(announcement);

        // Получаем списки email-ов участников для Kafka
        List<String> studentEmails = course.getEnrollments().stream()
                .map(enrollment -> enrollment.getStudent().getEmail())
                .toList();

        AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(
                saved.getId(),
                course.getId(),
                course.getTitle(),
                saved.getContent(),
                studentEmails
        );

        kafkaTemplate.send("announcement-events", event);

        return AnnouncementResponse.mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAnnouncementsByCourse(Long courseId) {
        return announcementRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(AnnouncementResponse::mapToResponse)
                .toList();
    }
}
