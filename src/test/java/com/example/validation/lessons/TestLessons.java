package com.example.validation.lessons;

import com.example.validation.dto.LessonsDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.model.Lessons;
import com.example.validation.repository.CoursesRepository;
import com.example.validation.repository.LessonsRepository;
import com.example.validation.service.LessonsService;
import com.example.validation.service.mapper.LessonsMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestLessons {
    private LessonsService lessonsService;
    private LessonsMapper lessonsMapper;
    private LessonsRepository lessonsRepository;
    private CoursesRepository coursesRepository;
    @BeforeEach
    void initObject(){
        lessonsMapper = mock(LessonsMapper.class);
        lessonsRepository = mock(LessonsRepository.class);
        coursesRepository = mock(CoursesRepository.class);
//        lessonsService = new LessonsService(lessonsMapper, lessonsRepository, coursesRepository);
    }

    @Test
    void testCreatePositive(){
        when(this.lessonsMapper.toEntity(any()))
                .thenReturn(Lessons.builder()
                        .lessonsId(1)
                        .title("Azizjon")
                        .status(true)
                        .build());
        when(this.lessonsMapper.toDto(any()))
                .thenReturn(LessonsDto.builder()
                        .lessonsId(1)
                        .title("Azizjon")
                        .status(true)
                        .build());

        ResponseDto<LessonsDto> responseDto =this.lessonsService.register(any());

        Assertions.assertEquals(responseDto.getCode(), 0);
        Assertions.assertTrue(responseDto.isSuccess());
        Assertions.assertNotNull(responseDto.getData());
        Assertions.assertNull(responseDto.getError());

        verify(this.lessonsMapper, times(1)).toDto(any());
        verify(this.lessonsRepository, times(1)).save(any());
    }

    @Test
    void testCreateNegative(){
        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<LessonsDto> response = this.lessonsService.get(any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());

        verify(this.lessonsRepository, times(1)).save(any());
    }

    @Test
    void testCreateException(){
        when(this.lessonsService.register(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<LessonsDto> responseDto = this.lessonsService.register(any());

        Assertions.assertNull(responseDto.getData());
        Assertions.assertEquals(-2, responseDto.getCode());
    }
    @Test
    void testGetPositive(){
        when(lessonsMapper.toDto(any()))
                .thenReturn(LessonsDto.builder()
                        .lessonsId(2)
                        .title("Hasanboy")
                        .status(true)
                        .build());

        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.ofNullable(Lessons.builder()
                        .lessonsId(1)
                        .title("Hasanboy")
                        .status(true)
                        .build()));

        ResponseDto<LessonsDto> response = this.lessonsService.get(any());

        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(response.getData().getCourseId(), 2);

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
        verify(this.lessonsMapper, times(1)).toDto(any());
    }
    @Test
    void testGetNegative(){
        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<LessonsDto> response = this.lessonsService.get(any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testUpdatePositive(){
        when(lessonsMapper.update(any(), any()))
                .thenReturn(Lessons.builder()
                        .lessonsId(2)
                        .title("Hasanboy")
                        .status(true)
                        .build());

        when(lessonsMapper.toDto(any()))
                .thenReturn(LessonsDto.builder()
                        .lessonsId(3)
                        .title("Hasanboy")
                        .status(true)
                        .build());

        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.ofNullable(Lessons.builder()
                        .lessonsId(1)
                        .title("Hasanboy")
                        .status(true)
                        .build()));

        ResponseDto<LessonsDto> response = this.lessonsService.update(any(), any());


        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getData().getCourseId(), 3);

        verify(this.lessonsMapper, times(1)).toDto(any());
        verify(this.lessonsMapper, times(1)).update(any(), any());
        verify(this.lessonsRepository, times(1)).save(any());
        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testUpdateNegative(){
        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<LessonsDto> response = this.lessonsService.update(any(), any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getMessage(), "Lessons are not found!");

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testUpdateException() {
        when(lessonsMapper.toDto(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<LessonsDto> response = this.lessonsService.update(any(), any());

        Assertions.assertEquals(response.getCode(), -2);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertNull(response.getData());
        Assertions.assertNull(response.getError());

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testDeletePositive(){
        when(lessonsMapper.toDto(any()))
                .thenReturn(LessonsDto.builder()
                        .lessonsId(3)
                        .title("Hasanboy")
                        .status(true)
                        .build());

        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.ofNullable(Lessons.builder()
                        .lessonsId(1)
                        .title("Hasanboy")
                        .status(true)
                        .build()));

        ResponseDto<LessonsDto> response = this.lessonsService.delete(any());

        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getData().getCourseId(), 3);

        verify(this.lessonsMapper, times(1)).toDto(any());
        verify(this.lessonsRepository, times(1)).save(any());
        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testDeleteNegative(){
        when(this.lessonsRepository.getLessonsById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<LessonsDto> response = this.lessonsService.delete(any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getMessage(), "Lessons are not found!");

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
    @Test
    void testDeleteException(){
        when(lessonsMapper.toDto(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<LessonsDto> response = this.lessonsService.delete(any());

        Assertions.assertEquals(response.getCode(), -2);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertNull(response.getData());
        Assertions.assertNull(response.getError());

        verify(this.lessonsRepository, times(1)).getLessonsById(any());
    }
}
