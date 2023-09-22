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
@Table(name = "lessons")
@NamedQueries(value = {
        @NamedQuery(name = "searchByNameThree", query = "select l from Lessons as l where l.title like concat(:val, '%') order by l.lessonsId asc"),
})
public class Lessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonsId;
    private Integer courseId;

    private String username;
    private String password;
    private Boolean enabled;
    private String code;

    @OneToMany(mappedBy = "lessonId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authorities> authorities;

    private String title;
    private String description;
    private String content;
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    private Courses courses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
