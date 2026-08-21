package org.example.classhub.services;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.classhub.DTOs.AttachmentDTOs.AttachmentResponse;
import org.example.classhub.models.Attachment;
import org.example.classhub.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final AttachmentRepository attachmentRepository;
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    @Transactional
    public AttachmentResponse saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Нельзя загрузить пустой файл");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFileName.contains("..")) {
            throw new IllegalArgumentException("Имя файла содержит недопустимый путь: " + originalFileName);
        }

        try {
            // Создаем уникальную папку для файла, чтобы избежать перезаписи при совпадении имён
            String uniqueSubDir = UUID.randomUUID().toString();
            Path targetLocation = Paths.get(uploadDir).resolve(uniqueSubDir).resolve(originalFileName);
            Files.createDirectories(targetLocation.getParent());

            // Копируем файл
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Сначала сохраняем запись без downloadUrl, чтобы получить ID
            Attachment attachment = Attachment.builder()
                    .originalFileName(originalFileName)
                    .filePath(targetLocation.toString())
                    .downloadUrl("")
                    .build();

            Attachment savedAttachment = attachmentRepository.save(attachment);

            // Генерируем публичную ссылку для скачивания на основе полученного ID
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(savedAttachment.getId().toString())
                    .toUriString();

            savedAttachment.setDownloadUrl(downloadUrl);
            attachmentRepository.save(savedAttachment);

            return AttachmentResponse.mapToResponse(savedAttachment);

        } catch (IOException e) {
            log.error("Ошибка при сохранении файла {}: {}", originalFileName, e.getMessage());
            throw new RuntimeException("Не удалось сохранить файл " + originalFileName, e);
        }
    }

    @Transactional(readOnly = true)
    public Resource loadFileAsResource(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден с ID: " + attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не найден или недоступен для чтения: " + attachment.getOriginalFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный путь к файлу: " + attachment.getOriginalFileName(), e);
        }
    }

    @Transactional(readOnly = true)
    public Attachment getAttachmentMetadata(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден с ID: " + attachmentId));
    }
}
