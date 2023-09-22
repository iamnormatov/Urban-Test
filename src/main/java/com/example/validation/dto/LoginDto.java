package com.example.validation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Username cannot be null or empty")
    private String username;
    @NotBlank(message = "Password cannot be null or empty")
    private String password;
}
