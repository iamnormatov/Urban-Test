package com.example.validation.model;

import com.example.validation.dto.LessonsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 1000 * 60 * 60 * 24 * 7)
public class LessonRefreshSession {
    @Id
    private String sessionId;
    private LessonsDto lessonsDto;
}
