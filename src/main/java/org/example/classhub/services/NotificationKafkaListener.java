package org.example.classhub.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.classhub.DTOs.Events.AnnouncementCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "announcement-events", groupId = "classhub-notification-group")
    public void handleAnnouncementCreated(AnnouncementCreatedEvent event) {
        log.info("Получено событие для рассылки по курсу: {}", event.courseTitle());

        for (String email : event.studentEmails()) {
            try {
                sendEmail(email, event.courseTitle(), event.announcementContent());
            } catch (Exception e) {
                log.error("Ошибка при отправке email на адрес {}: {}", email, e.getMessage());
            }
        }
    }

    private void sendEmail(String toEmail, String courseTitle, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Новое объявление в курсе: " + courseTitle);
        message.setText("В вашем курсе \"" + courseTitle + "\" опубликовано новое объявление:\n\n" + content);

        mailSender.send(message);
    }
}