package com.example.validation.service;

import java.util.List;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.example.validation.model.Courses;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.dto.CoursesDto;
import com.example.validation.dto.ResponseDto;
import org.springframework.stereotype.Service;
import com.example.validation.repository.CoursesRepository;
import com.example.validation.service.mapper.CoursesMapper;

@Service
@RequiredArgsConstructor
public class CoursesService implements SimpleCRUD<Integer, CoursesDto> {

    private final CoursesMapper coursesMapper;
    private final CoursesRepository coursesRepository;

    @Override
    public ResponseDto<CoursesDto> create(CoursesDto dto) {
        try {
            Courses courses = this.coursesMapper.toEntity(dto);
            courses.setCreatedAt(LocalDateTime.now());
            this.coursesRepository.save(courses);
            return ResponseDto.<CoursesDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.coursesMapper.toDto(courses))
                    .build();
        }catch (Exception e){
            return ResponseDto.<CoursesDto>builder()
                    .code(-2)
                    .message(String.format("Courses while saving error %s", e.getMessage()))
                    .build();
        }
    }

    @Override
    public ResponseDto<CoursesDto> get(Integer entityId) {
        return this.coursesRepository.getCoursesById(entityId)
                .map(courses -> ResponseDto.<CoursesDto>builder()
                        .success(true)
                        .message("OK")
                        .data(this.coursesMapper.toDtoWithCard(courses))
                        .build()
                )
                .orElse(ResponseDto.<CoursesDto>builder()
                        .code(-1)
                        .message("Courses are not found!")
                        .build()
                );
    }

    @Override
    public ResponseDto<CoursesDto> update(CoursesDto dto, Integer entityId) {
        try {
            return this.coursesRepository.getCoursesById(entityId)
                    .map(courses -> {
                        this.coursesMapper.update(dto, courses);
                        courses.setUpdatedAt(LocalDateTime.now());
                        this.coursesRepository.save(courses);
                        return ResponseDto.<CoursesDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.coursesMapper.toDto(courses))
                                .build();
                    })
                    .orElse(ResponseDto.<CoursesDto>builder()
                            .code(-1)
                            .message("Courses are not found!")
                            .build()
                    );
        }catch (Exception e){
            return ResponseDto.<CoursesDto>builder()
                    .code(-2)
                    .message(String.format("Course while update error %s", e.getMessage()))
                    .build();
        }
    }

    @Override
    public ResponseDto<CoursesDto> delete(Integer entityId) {
        try {
            return this.coursesRepository.getCoursesById(entityId)
                    .map(courses -> {
                        courses.setDeletedAt(LocalDateTime.now());
                        this.coursesRepository.save(courses);
                        return ResponseDto.<CoursesDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.coursesMapper.toDto(courses))
                                .build();
                    })
                    .orElse(ResponseDto.<CoursesDto>builder()
                            .code(-1)
                            .message("Courses are not found!")
                            .build()
                    );
        }catch (Exception e){
            return ResponseDto.<CoursesDto>builder()
                    .code(-2)
                    .message(String.format("Course while delete error %s", e.getMessage()))
                    .build();
        }
    }

    public ResponseDto<List<CoursesDto>> searchUserByName(String value) {
        List<Courses> coursesList = this.coursesRepository.searchUserByName(value);
        if (coursesList.isEmpty()) {
            return ResponseDto.<List<CoursesDto>>builder()
                    .code(-1)
                    .message("Courses are not found")
                    .build();
        }
        return ResponseDto.<List<CoursesDto>>builder()
                .success(true)
                .message("OK")
                .data(coursesList.stream().map(this.coursesMapper::toDto).toList())
                .build();
    }
}
