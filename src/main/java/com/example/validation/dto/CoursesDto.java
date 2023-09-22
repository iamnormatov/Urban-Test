package com.example.validation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoursesDto {
    private Integer courseId;

    @NotBlank(message = "Type cannot be null or empty!")
    private String type;
    @NotBlank(message = "Name cannot be null or empty!")
    private String name;
    @NotBlank(message = "Description cannot be null or empty!")
    private String description;

    private Integer perWeek;
    private Integer durationMonth;
    private Integer durationDays;
    private Integer durationHours;
    private String intendedStudents;
    private String outcomes;
    private String benefits;
    private String faq;
    private String status;

    private Set<LessonsDto> lessons;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
