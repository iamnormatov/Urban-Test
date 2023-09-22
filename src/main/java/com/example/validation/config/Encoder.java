package com.example.validation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class Encoder {

    @Bean
    public PasswordEncoder funEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

