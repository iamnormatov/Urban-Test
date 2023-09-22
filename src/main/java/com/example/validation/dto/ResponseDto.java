package com.example.validation.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private String message;
    private Integer code;
    private T data;
    private List<ErrorDto> error;
}


