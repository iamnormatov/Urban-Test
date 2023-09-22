package com.example.validation.controller;

import com.example.validation.dto.CoursesDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.service.CoursesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "courses")
public class CoursesController implements SimpleCRUD<Integer, CoursesDto> {

    private final CoursesService coursesService;

    @PostMapping(value = "/create")
    @Operation(
            method = "Method",
            summary = "Summary",
            description = "It's first description "
    )
    @Tag(
            name = "Courses controller",
            description = "Welcome to my project"
    )
    @Override
    public ResponseDto<CoursesDto> create(@RequestBody @Valid CoursesDto dto) {
        return this.coursesService.create(dto);
    }

    @GetMapping(value = "/get/{id}")
    @Override
    public ResponseDto<CoursesDto> get(@PathVariable(name = "id") Integer entityId) {
        return this.coursesService.get(entityId);
    }

    @PutMapping(value = "/update/{id}")
    @Override
    public ResponseDto<CoursesDto> update(@RequestBody @Valid CoursesDto dto, @PathVariable(name = "id") Integer entityId) {
        return this.coursesService.update(dto, entityId);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseDto<CoursesDto> delete(@PathVariable(name = "id") Integer entityId) {
        return this.coursesService.delete(entityId);
    }

    @GetMapping(value = "/search-all-name")
    public ResponseDto<List<CoursesDto>> searchUserByName(@RequestParam String value){
        return this.coursesService.searchUserByName(value);
    }
}
