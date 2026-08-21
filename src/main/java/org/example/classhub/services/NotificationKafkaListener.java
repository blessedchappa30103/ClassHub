package org.example.classhub.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.classhub.DTOs.Events.AnnouncementCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @KafkaListener(topics = "announcement-events", groupId = "classhub-notification-group")
    public void handleAnnouncementCreated(AnnouncementCreatedEvent event) {
        log.info("Получено событие для рассылки по курсу: {}", event.courseTitle());

        for (String email : event.studentEmails()) {
            try {
                sendEmail(email, event.courseTitle(), event.announcementContent(), event.attachmentUrls());
            } catch (Exception e) {
                log.error("Ошибка при отправке email на адрес {}: {}", email, e.getMessage());
            }
        }
    }

    private void sendEmail(String toEmail, String courseTitle, String content, List<String> attachmentUrls) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);

        // Для локального теста вы захардкодили свой email (или передавайте toEmail)
        message.setTo("login.filippov200@yandex.ru");

        message.setSubject("Новое объявление в курсе: " + courseTitle);

        StringBuilder body = new StringBuilder();
        body.append("В вашем курсе \"").append(courseTitle).append("\" опубликовано новое объявление:\n\n");
        body.append(content).append("\n\n");

        if (attachmentUrls != null && !attachmentUrls.isEmpty()) {
            body.append("Прикрепленные материалы:\n");
            for (String url : attachmentUrls) {
                body.append("- ").append(url).append("\n");
            }
        }

        message.setText(body.toString());
        mailSender.send(message);
    }
}