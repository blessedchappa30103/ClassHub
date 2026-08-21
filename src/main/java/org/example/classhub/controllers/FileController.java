package org.example.classhub.controllers;

import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.classhub.DTOs.AttachmentDTOs.AttachmentResponse;
import org.example.classhub.models.Attachment;
import org.example.classhub.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<AttachmentResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        AttachmentResponse attachment = fileService.saveFile(file);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(id);
        Attachment attachment = fileService.getAttachmentMetadata(id);

        // Определяем MIME-тип файла
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Не удалось определить тип файла.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Кодируем имя файла для корректного отображения кириллицы при скачивании
        String encodedFileName = URLEncoder.encode(attachment.getOriginalFileName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }
}
