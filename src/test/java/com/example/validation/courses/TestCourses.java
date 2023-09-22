package com.example.validation.courses;

import com.example.validation.dto.CoursesDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.model.Courses;
import com.example.validation.repository.CoursesRepository;
import com.example.validation.service.CoursesService;
import com.example.validation.service.mapper.CoursesMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestCourses {
    private CoursesService coursesService;
    private CoursesMapper coursesMapper;
    private CoursesRepository coursesRepository;
    @BeforeEach
    void initObject(){
        coursesMapper = mock(CoursesMapper.class);
        coursesRepository = mock(CoursesRepository.class);
        coursesService = new CoursesService(coursesMapper, coursesRepository);
    }

    @Test
    void testCreatePositive(){
        when(this.coursesMapper.toEntity(any()))
                .thenReturn(Courses.builder()
                        .courseId(1)
                        .name("Azizjon")
                        .status("true")
                        .build());
        when(this.coursesMapper.toDto(any()))
                .thenReturn(CoursesDto.builder()
                        .courseId(1)
                        .name("Azizjon")
                        .status("true")
                        .build());

        ResponseDto<CoursesDto> responseDto =this.coursesService.create(any());

        Assertions.assertEquals(0,responseDto.getCode());
        Assertions.assertTrue(responseDto.isSuccess());
        Assertions.assertNotNull(responseDto.getData());
        Assertions.assertNull(responseDto.getError());

        verify(this.coursesMapper, times(1)).toDto(any());
        verify(this.coursesRepository, times(1)).save(any());
    }
    @Test
    void testCreateException(){
        when(this.coursesService.create(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<CoursesDto> responseDto = this.coursesService.create(any());

        Assertions.assertNull(responseDto.getData());
        Assertions.assertEquals(-2, responseDto.getCode());
    }
    @Test
    void testGetPositive(){
        when(coursesMapper.toDto(any()))
                .thenReturn(CoursesDto.builder()
                        .courseId(2)
                        .name("Azizjon")
                        .status("true")
                        .build());

        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.ofNullable(Courses.builder()
                        .courseId(1)
                        .name("Azizjon")
                        .status("true")
                        .build()));

        ResponseDto<CoursesDto> response = this.coursesService.get(any());

        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(response.getData().getCourseId(), 2);

        verify(this.coursesRepository, times(1)).getCoursesById(any());
        verify(this.coursesMapper, times(1)).toDto(any());
    }
    @Test
    void testGetNegative(){
        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<CoursesDto> response = this.coursesService.get(any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());

        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testUpdatePositive(){
        when(coursesMapper.update(any(), any()))
                .thenReturn(Courses.builder()
                        .courseId(2)
                        .name("Azizjon")
                        .status("true")
                        .build());

        when(coursesMapper.toDto(any()))
                .thenReturn(CoursesDto.builder()
                        .courseId(3)
                        .name("Azizjon")
                        .status("true")
                        .build());

        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.ofNullable(Courses.builder()
                        .courseId(1)
                        .name("Azizjon")
                        .status("true")
                        .build()));

        ResponseDto<CoursesDto> response = this.coursesService.update(any(), any());


        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getData().getCourseId(), 3);

        verify(this.coursesMapper, times(1)).toDto(any());
        verify(this.coursesMapper, times(1)).update(any(), any());
        verify(this.coursesRepository, times(1)).save(any());
        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testUpdateNegative(){
        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<CoursesDto> response = this.coursesService.update(any(), any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getMessage(), "Course is not found!");

        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testUpdateException() {
        when(coursesMapper.toDto(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<CoursesDto> response = this.coursesService.update(any(), any());

        Assertions.assertEquals(response.getCode(), -2);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertNull(response.getData());
        Assertions.assertNull(response.getError());

        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testDeletePositive(){
        when(coursesMapper.toDto(any()))
                .thenReturn(CoursesDto.builder()
                        .courseId(3)
                        .name("Azizjon")
                        .status("true")
                        .build());

        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.ofNullable(Courses.builder()
                        .courseId(1)
                        .name("Azizjon")
                        .status("true")
                        .build()));

        ResponseDto<CoursesDto> response = this.coursesService.delete(any());

        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getData().getCourseId(), 3);

        verify(this.coursesMapper, times(1)).toDto(any());
        verify(this.coursesRepository, times(1)).save(any());
        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testDeleteNegative(){
        when(this.coursesRepository.getCoursesById(any()))
                .thenReturn(Optional.empty());

        ResponseDto<CoursesDto> response = this.coursesService.delete(any());

        Assertions.assertEquals(response.getCode(), -1);
        Assertions.assertNull(response.getData());
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getMessage(), "Course is not found!");

        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
    @Test
    void testDeleteException(){
        when(coursesMapper.toDto(any()))
                .thenThrow(RuntimeException.class);

        ResponseDto<CoursesDto> response = this.coursesService.delete(any());

        Assertions.assertEquals(response.getCode(), -2);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertNull(response.getData());
        Assertions.assertNull(response.getError());

        verify(this.coursesRepository, times(1)).getCoursesById(any());
    }
}
