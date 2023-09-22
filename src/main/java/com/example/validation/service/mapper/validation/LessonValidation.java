package com.example.validation.service.mapper.validation;

import com.example.validation.dto.ErrorDto;
import com.example.validation.dto.LessonsDto;
import com.example.validation.repository.LessonsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonValidation {

    private final LessonsRepository lessonsRepository;
    public List<ErrorDto> lessonValid(LessonsDto dto) {
        List<ErrorDto> errors = new ArrayList<>();
        if (this.lessonsRepository.existsByUsernameAndEnabledTrueAndDeletedAtIsNull(dto.getUsername())){
            errors.add(new ErrorDto("username", String.format("Lesson with username %s already exist", dto.getUsername())));
        }
        return errors;
    }
}
