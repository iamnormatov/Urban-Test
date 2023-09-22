package com.example.validation.service.mapper;

import com.example.validation.dto.LessonsDto;
import com.example.validation.model.Courses;
import com.example.validation.model.Lessons;
import com.example.validation.service.CoursesService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public class LessonsMapper {

    @Autowired
    protected CoursesService coursesService;

    @Autowired
    protected CoursesMapper coursesMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public Lessons toEntity(LessonsDto dto){
        return Lessons.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(false)
                .code("0000")
                .build();
    };

    public LessonsDto toDto(Lessons lessons){
        return LessonsDto.builder()
                .lessonsId(lessons.getLessonsId())
                .username(lessons.getUsername())
                .password(lessons.getPassword())
                .enabled(lessons.getEnabled())
                .createdAt(lessons.getCreatedAt())
                .updatedAt(lessons.getUpdatedAt())
                .deletedAt(lessons.getDeletedAt())
                .build();
    };

    public LessonsDto toDtoWithAuthorities(Lessons lessons){
        return LessonsDto.builder()
                .lessonsId(lessons.getLessonsId())
                .username(lessons.getUsername())
                .password(lessons.getPassword())
                .enabled(lessons.getEnabled())
                .authorities(lessons.getAuthorities())
                .createdAt(lessons.getCreatedAt())
                .updatedAt(lessons.getUpdatedAt())
                .deletedAt(lessons.getDeletedAt())
                .build();
    }

    public LessonsDto toDtoWithCourses(Lessons lessons){
        return LessonsDto.builder()
                .lessonsId(lessons.getLessonsId())
                .username(lessons.getUsername())
                .password(lessons.getPassword())
                .enabled(lessons.getEnabled())
                .coursesDto(this.coursesService.get(lessons.getCourseId()).getData())
                .createdAt(lessons.getCreatedAt())
                .updatedAt(lessons.getUpdatedAt())
                .deletedAt(lessons.getDeletedAt())
                .build();
    };

    public Lessons update(LessonsDto dto, Lessons lessons){
        if ( dto == null ) {
            return lessons;
        }

        if ( dto.getLessonsId() != null ) {
            lessons.setLessonsId( dto.getLessonsId() );
        }
        if ( dto.getCourseId() != null ) {
            lessons.setCourseId( dto.getCourseId() );
        }
        if ( dto.getUsername() != null ) {
            lessons.setUsername( dto.getUsername() );
        }
        if ( dto.getPassword() != null ) {
            lessons.setPassword( dto.getPassword() );
        }
        if ( dto.getEnabled() != null ) {
            lessons.setEnabled( dto.getEnabled() );
        }
        if ( dto.getTitle() != null ) {
            lessons.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            lessons.setDescription( dto.getDescription() );
        }
        if ( dto.getContent() != null ) {
            lessons.setContent( dto.getContent() );
        }
        lessons.setStatus( dto.isStatus() );
        if ( dto.getUpdatedAt() != null ) {
            lessons.setUpdatedAt( dto.getUpdatedAt() );
        }

        return lessons;
    };

}
