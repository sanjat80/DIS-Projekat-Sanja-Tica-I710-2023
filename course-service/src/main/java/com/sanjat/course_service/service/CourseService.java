package com.sanjat.course_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sanjat.course_service.model.Course;
import com.sanjat.course_service.repository.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return repository.findById(id);
    }

    public Course save(Course course) {
        return repository.save(course);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean hasVacancy(Long courseId, int currentEnrolled) {
        Optional<Course> courseOpt = repository.findById(courseId);
        if (courseOpt.isEmpty())
            return false;
        return currentEnrolled < courseOpt.get().getCapacity();
    }

    public boolean isApplicationPeriodOpen(Long courseId) {
        Optional<Course> courseOpt = repository.findById(courseId);
        if (courseOpt.isEmpty())
            return false;
        LocalDate now = LocalDate.now();
        Course course = courseOpt.get();
        return !now.isBefore(course.getApplicationStart()) && !now.isAfter(course.getApplicationEnd());
    }

    public boolean hasMinimumPoints(Long courseId, int points) {
        Optional<Course> courseOpt = repository.findById(courseId);
        if (courseOpt.isEmpty())
            return false;
        return points >= courseOpt.get().getMinPoints();
    }

    public List<Course> findAllWithOpenApplicationPeriod() {
        LocalDate now = LocalDate.now();
        return repository.findAll().stream()
                .filter(course -> !now.isBefore(course.getApplicationStart()) &&
                        !now.isAfter(course.getApplicationEnd()))
                .collect(Collectors.toList());
    }

    public Long getCourseIdByName(String name) {
        Course course = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Kurs sa datim imenom ne postoji!"));

        return course.getId();
    }
}
