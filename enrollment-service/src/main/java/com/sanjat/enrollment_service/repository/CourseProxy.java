package com.sanjat.enrollment_service.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sanjat.enrollment_service.dtos.CourseDto;

@FeignClient(name = "course-service")
public interface CourseProxy {
    @GetMapping("/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") Long id);

    // Provera da li ima slobodnih mesta na kursu
    @GetMapping("/courses/{id}/has-vacancy")
    Boolean hasVacancy(@PathVariable("id") Long id, @RequestParam("currentEnrolled") int currentEnrolled);

    // Provera da li student ima minimalni broj poena
    @GetMapping("/courses/{id}/min-points")
    Boolean hasMinimumPoints(@PathVariable("id") Long id, @RequestParam("points") int points);

    // Vrati sve kurseve sa otvorenom prijavom
    @GetMapping("/courses/open-applications")
    List<CourseDto> getCoursesWithOpenApplications();

    @GetMapping("/courses/id")
    Long getCourseIdByName(@RequestParam String name);
}