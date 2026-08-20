package org.example.classhub.repository;

import org.example.classhub.models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByCourseIdOrderByCreatedAtDesc(Long courseId);
}
