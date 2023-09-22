package com.example.validation.repository;

import com.example.validation.model.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonsRepository extends JpaRepository<Lessons, Integer> {
    @Query(
            value = "select * from lessons where lessons_id = ?1",
            nativeQuery = true
    )
    Optional<Lessons> getLessonsById(Integer lessonsId);

    @Query(name = "searchByNameThree")
    List<Lessons> searchUserByName(@Param(value = "val") String value);

    Optional<Lessons> findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(String username);

    Optional<Lessons> findByUsernameAndDeletedAtIsNull(String username);

    boolean existsByUsernameAndEnabledTrueAndDeletedAtIsNull(String username);
}
