package com.example.validation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
@NamedQueries(value = {
        @NamedQuery(name = "searchByNameOne",
                    query = "select c from Courses as c where c.name like concat(:val, '%') order by c.courseId asc"),
})
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    private String type;
    private String name;
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

    @OneToMany(mappedBy = "courseId", fetch = FetchType.EAGER)
    private Set<Lessons> lessons;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
