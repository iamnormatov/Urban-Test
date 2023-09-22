package com.example.validation.service;

import com.example.validation.dto.*;
import com.example.validation.model.*;
import com.example.validation.repository.*;
import com.example.validation.security.JwtUtils;
import com.example.validation.service.mapper.LessonsMapper;
import com.example.validation.service.mapper.validation.LessonValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonsService implements UserDetailsService {

    private final JwtUtils jwtUtils;
    private final LessonsMapper lessonsMapper;
    private final LessonValidation lessonValidation;
    private final AccessRepository accessRepository;
    private final LessonsRepository lessonsRepository;
    private final CoursesRepository coursesRepository;
    private final RefreshRepository refreshRepository;
    private final AuthoritiesRepository authoritiesRepository;

    public ResponseDto<LessonsDto> register(LessonsDto dto) {
//        Optional<Courses> optional = this.coursesRepository.getCoursesById(dto.getCourseId());
//        if (optional.isEmpty()){
//            return ResponseDto.<LessonsDto>builder()
//                    .code(-1)
//                    .message("Courses are not found!")
//                    .build();
//        }
        List<ErrorDto> lessonErrors = this.lessonValidation.lessonValid(dto);
        if (!lessonErrors.isEmpty()){
            return ResponseDto.<LessonsDto>builder()
                    .code(-3)
                    .message("Validation error!")
                    .error(lessonErrors)
                    .build();
        }

        try {
            var entity = this.lessonsMapper.toEntity(dto);
            entity.setCreatedAt(LocalDateTime.now());
            return ResponseDto.<LessonsDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.lessonsMapper.toDtoWithAuthorities(
                            this.lessonsRepository.save(entity)

                    ))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<LessonsDto>builder()
                    .code(-2)
                    .message(String.format("Lessons while saving error %s", e.getMessage()))
                    .build();
        }
    }

    @Transactional
    public ResponseDto<TokenResponseDto> registerConfirm(RegisterConfirmDto dto) {
        return this.lessonsRepository.findByUsernameAndDeletedAtIsNull(dto.getUsername())
                .map(lessons -> {
                    lessons.setEnabled(true);
                    Lessons save = this.lessonsRepository.save(lessons);
                    String newSubject = toJsonByLesson(save);

                    this.authoritiesRepository.save(Authorities.builder()
                            .lessonId(save.getLessonsId())
                            .authority("User")
                            .username(save.getUsername())
                            .build()
                    );
                    checkValidToken(newSubject);

                    saveLessonSession(newSubject, this.lessonsMapper.toDto(lessons));

                    var simpleLessonToken = this.jwtUtils.generateToken(newSubject);
                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .data(TokenResponseDto.builder()
                                    .accessToken(simpleLessonToken)
                                    .refreshToken(simpleLessonToken)
                                    .build())
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("Lessons with %s username is not found!", dto.getUsername()))
                        .build()
                );

    }

    @Transactional
    public ResponseDto<TokenResponseDto> login(LessonsDto dto) {
        return this.lessonsRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(dto.getUsername())
                .map(lessons -> {
                    String newSubject = toJsonByLesson(lessons);

                    checkValidToken(newSubject);

                    saveLessonSession(newSubject, this.lessonsMapper.toDto(lessons));

                    var simpleLessonToken = this.jwtUtils.generateToken(newSubject);
                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .data(TokenResponseDto.builder()
                                    .accessToken(simpleLessonToken)
                                    .refreshToken(simpleLessonToken)
                                    .build())
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("Lessons with %s username is not found!", dto.getUsername()))
                        .build()
                );
    }

    public ResponseDto<TokenResponseDto> refreshToken(String token) {
        if (!this.jwtUtils.isValid(token)) return ResponseDto.<TokenResponseDto>builder()
                .code(-3)
                .message("Token is not valid")
                .build();

        return this.refreshRepository.findById(token)
                .map(lessonAccessSession -> {
                    checkValidToken(token);
                    LessonsDto lessonsDto = lessonAccessSession.getLessonsDto();
                    Lessons entity = this.lessonsMapper.toEntity(lessonsDto);
                    entity.setEnabled(true);
                    String newSubject = toJsonByLesson(entity);

                    saveLessonSession(newSubject, this.lessonsMapper.toDto(entity));

                    var newToken = this.jwtUtils.generateToken(newSubject);
                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .data(TokenResponseDto.builder()
                                    .accessToken(newToken)
                                    .refreshToken(newToken)
                                    .build())
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("Lesson with credential token %s is not found", token))
                        .build()
                );


    }

    public ResponseDto<TokenResponseDto> logout(LoginDto dto) {
        return this.lessonsRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(dto.getUsername())
                .map(lessons -> {
                    lessons.setEnabled(false);
                    this.lessonsRepository.save(lessons);
                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("Lessons with %s username is not found!", dto.getUsername()))
                        .build()
                );
    }

    private void saveLessonSession(String sessionId, LessonsDto lessonsDto) {
        this.accessRepository.save(new LessonAccessSession(
                        sessionId, lessonsDto
                )
        );
        this.refreshRepository.save(new LessonRefreshSession(
                        sessionId, lessonsDto
                )
        );
    }

    private void checkValidToken(String token) {
        this.accessRepository.findById(token)
                .ifPresent(this.accessRepository::delete);
        this.refreshRepository.findById(token)
                .ifPresent(this.refreshRepository::delete);
    }


    public ResponseDto<LessonsDto> get(Integer entityId) {
        return this.lessonsRepository.getLessonsById(entityId)
                .map(courses -> ResponseDto.<LessonsDto>builder()
                        .success(true)
                        .message("OK")
                        .data(this.lessonsMapper.toDtoWithCourses(courses))
                        .build()
                )
                .orElse(ResponseDto.<LessonsDto>builder()
                        .code(-1)
                        .message("Lessons are not found!")
                        .build()
                );
    }

    public ResponseDto<LessonsDto> update(LessonsDto dto, Integer entityId) {
        try {
            return this.lessonsRepository.getLessonsById(entityId)
                    .map(courses -> {
                        this.lessonsMapper.update(dto, courses);
                        courses.setUpdatedAt(LocalDateTime.now());
                        this.lessonsRepository.save(courses);
                        return ResponseDto.<LessonsDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.lessonsMapper.toDto(courses))
                                .build();
                    })
                    .orElse(ResponseDto.<LessonsDto>builder()
                            .code(-1)
                            .message("Lessons are not found!")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseDto.<LessonsDto>builder()
                    .code(-2)
                    .message(String.format("Lessons while update error %s", e.getMessage()))
                    .build();
        }
    }

    public ResponseDto<LessonsDto> delete(Integer entityId) {
        try {
            return this.lessonsRepository.getLessonsById(entityId)
                    .map(courses -> {
                        courses.setDeletedAt(LocalDateTime.now());
                        this.lessonsRepository.save(courses);
                        return ResponseDto.<LessonsDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.lessonsMapper.toDto(courses))
                                .build();
                    })
                    .orElse(ResponseDto.<LessonsDto>builder()
                            .code(-1)
                            .message("Lessons are not found!")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseDto.<LessonsDto>builder()
                    .code(-2)
                    .message(String.format("Lessons while delete error %s", e.getMessage()))
                    .build();
        }
    }

    public ResponseDto<List<LessonsDto>> searchUserByName(String value) {
        List<Lessons> lessonsList = this.lessonsRepository.searchUserByName(value);
        if (lessonsList.isEmpty()) {
            return ResponseDto.<List<LessonsDto>>builder()
                    .code(-1)
                    .message("Lessons are not found")
                    .build();
        }
        return ResponseDto.<List<LessonsDto>>builder()
                .success(true)
                .message("OK")
                .data(lessonsList.stream().map(this.lessonsMapper::toDto).toList())
                .build();
    }

    @Override
    public LessonsDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.lessonsRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(username)
                .map(this.lessonsMapper::toDtoWithAuthorities)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("Lessons with %s username is not found", username)
                        )
                );
    }

    public String toJsonByLesson(Lessons dto) {
        return
                "lessonsId-" + dto.getLessonsId() +
                        ", username-'" + dto.getUsername() + '\'' +
                        ", enabled-" + dto.getEnabled();
    }
}
