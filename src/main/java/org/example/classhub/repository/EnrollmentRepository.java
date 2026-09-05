package org.example.classhub.repository;

import org.example.classhub.models.Enrollment;
import org.example.classhub.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    List<Enrollment> findAllByCourseIdAndStatus(Long courseId, Status status);
    @Modifying
    @Query("UPDATE Enrollment e SET e.status = :newStatus WHERE e.course.id = :courseId AND e.status = :oldStatus")
    void updateEnrollmentStatusForCourse(
            @Param("courseId") Long courseId,
            @Param("oldStatus") Status oldStatus,
            @Param("newStatus") Status newStatus
    );
}
