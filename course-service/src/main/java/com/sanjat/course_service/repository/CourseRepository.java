package com.sanjat.course_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sanjat.course_service.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}