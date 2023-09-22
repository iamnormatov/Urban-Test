package com.example.validation.controller;

import com.example.validation.dto.*;
import com.example.validation.service.LessonsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "lessons")
public class LessonsController implements SimpleCRUD<Integer, LessonsDto> {

    private final LessonsService lessonsService;

    @PostMapping(value = "/register")
    @Operation(
            method = "Method",
            summary = "Summary",
            description = "It's first description "
    )
    @Tag(
            name = "Lessons controller",
            description = "Welcome to my project"
    )
    @Override
    public ResponseDto<LessonsDto> create(@RequestBody @Valid LessonsDto dto) {
        return this.lessonsService.register(dto);
    }

    @PostMapping(value = "/register-confirm")
    public ResponseDto<TokenResponseDto> registerConfirm(@RequestBody @Valid RegisterConfirmDto dto){
        return this.lessonsService.registerConfirm(dto);
    }

    @PostMapping(value = "/login")
    public ResponseDto<TokenResponseDto> login(@RequestBody @Valid LessonsDto dto){
        return this.lessonsService.login(dto);
    }

    @PostMapping(value = "/refresh-token")
    public ResponseDto<TokenResponseDto> refreshToken(@RequestParam(value = "token") String token){
        return this.lessonsService.refreshToken(token);
    }

    @PostMapping(value = "/logout")
    public ResponseDto<TokenResponseDto> logout(@RequestBody @Valid LoginDto dto){
        return this.lessonsService.logout(dto);
    }



    @GetMapping(value = "/get/{id}")
    @Override
    public ResponseDto<LessonsDto> get(@PathVariable(name = "id") Integer entityId) {
        return this.lessonsService.get(entityId);
    }

    @PutMapping(value = "/update/{id}")
    @Override
    public ResponseDto<LessonsDto> update(@RequestBody @Valid LessonsDto dto, @PathVariable(name = "id") Integer entityId) {
        return this.lessonsService.update(dto, entityId);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseDto<LessonsDto> delete(@PathVariable(name = "id") Integer entityId) {
        return this.lessonsService.delete(entityId);
    }

    @GetMapping(value = "/search-all-name")
    public ResponseDto<List<LessonsDto>> searchUserByName(@RequestParam String value){
        return this.lessonsService.searchUserByName(value);
    }
}
