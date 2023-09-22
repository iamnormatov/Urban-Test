package com.example.validation.dto;

import com.example.validation.model.Authorities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class LessonsDto implements UserDetails {
    private Integer lessonsId;
    private Integer courseId;
    @NotBlank(message = "Username cannot be null or empty")
    private String username;
    @NotBlank(message = "Password cannot be null or empty")
    private String password;
    private Boolean enabled;


    private Set<Authorities> authorities;

    private CoursesDto coursesDto;

//    @NotBlank(message = "Title cannot be null or empty!")
    private String title;
//    @NotBlank(message = "Description cannot be null or empty!")
    private String description;
    private boolean status;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(authorities)
                .map(auth -> auth.stream()
                        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                        .toList()
                )
                .orElse(new ArrayList<>());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.enabled;
    }
}
