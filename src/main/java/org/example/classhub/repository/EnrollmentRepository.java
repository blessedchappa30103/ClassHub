package org.example.classhub.repository;

import org.example.classhub.models.Enrollment;
import org.example.classhub.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    List<Enrollment> findAllByCourseIdAndStatus(Long courseId, Status status);
}
