package com.example.validation.repository;

import com.example.validation.model.Courses;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer> {
    @Query(
            value = "select * from course where course_id = ?1",
            nativeQuery = true
    )
    Optional<Courses> getCoursesById(Integer courseId);

    @Query(name = "searchByNameOne")
    List<Courses> searchUserByName(@Param(value = "val") String value);
}
