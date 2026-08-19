package org.example.classhub.repository;

import org.example.classhub.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);
    Optional<Course> findByJoinCode(String code);
}
