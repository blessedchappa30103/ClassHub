package org.example.classhub.DTOs.CourseDTOs;

import org.example.classhub.models.Course;

public record CourseResponse(
        Long id,
        String title,
        String joinCode
) {
    public static CourseResponse mapToResponse(Course course){
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getJoinCode()
        );
    }
}
