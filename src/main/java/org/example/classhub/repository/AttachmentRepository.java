package org.example.classhub.repository;

import org.example.classhub.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByIdIn(List<Long> ids);
}
