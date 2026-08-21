package org.example.classhub.services;

import lombok.RequiredArgsConstructor;
import org.example.classhub.DTOs.AnnouncementDTOs.AnnouncementResponse;
import org.example.classhub.DTOs.AnnouncementDTOs.CreateAnnouncementRequest;
import org.example.classhub.DTOs.Events.AnnouncementCreatedEvent;
import org.example.classhub.models.Announcement;
import org.example.classhub.models.Attachment;
import org.example.classhub.models.Course;
import org.example.classhub.models.User;
import org.example.classhub.repository.AnnouncementRepository;
import org.example.classhub.repository.AttachmentRepository;
import org.example.classhub.repository.CourseRepository;
import org.example.classhub.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
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

        // 1. Привязываем файлы по их ID, если они переданы
        List<Attachment> attachments = new ArrayList<>();
        if (request.attachmentIds() != null && !request.attachmentIds().isEmpty()) {
            attachments = attachmentRepository.findAllByIdIn(request.attachmentIds());
            for (Attachment attachment : attachments) {
                attachment.setAnnouncement(announcement);
            }
            announcement.setAttachments(attachments);
        }

        Announcement saved = announcementRepository.save(announcement);

        // 2. Формируем список ссылок на файлы для Kafka
        List<String> attachmentUrls = saved.getAttachments().stream()
                .map(Attachment::getDownloadUrl)
                .toList();

        // 3. Получаем список email-ов участников
        List<String> studentEmails = course.getEnrollments().stream()
                .map(enrollment -> enrollment.getStudent().getEmail())
                .toList();

        // 4. Публикуем событие с файлами
        AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(
                saved.getId(),
                course.getId(),
                course.getTitle(),
                saved.getContent(),
                studentEmails,
                attachmentUrls
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
