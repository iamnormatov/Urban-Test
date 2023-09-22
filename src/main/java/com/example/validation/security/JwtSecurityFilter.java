package com.example.validation.security;

import com.example.validation.repository.AccessRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AccessRepository accessRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isBlank(authorization) && authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            if (this.jwtUtils.isValid(token)){
                this.accessRepository.findById(this.jwtUtils.getClaims(token, "sub", String.class))
                        .ifPresent(lessonAccessSession -> {
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                                    lessonAccessSession.getLessonsDto(),
                                    lessonAccessSession.getLessonsDto().getPassword(),
                                    lessonAccessSession.getLessonsDto().getAuthorities()
                            ));
                        });
            }
        }
        filterChain.doFilter(request, response);
    }
}

