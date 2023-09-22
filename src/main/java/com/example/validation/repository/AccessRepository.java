package com.example.validation.repository;

import com.example.validation.model.LessonAccessSession;
import com.example.validation.model.Lessons;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends CrudRepository<LessonAccessSession, String> {
}
