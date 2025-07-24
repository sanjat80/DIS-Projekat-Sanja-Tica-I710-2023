package com.sanjat.enrollment_service.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sanjat.enrollment_service.config.FeignClientConfig;
import com.sanjat.enrollment_service.dtos.CourseDto;

@FeignClient(name = "course-service", configuration = FeignClientConfig.class)
public interface CourseProxy {
    @GetMapping("/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") Long id);

    @GetMapping("/courses/internal/{id}/has-capacity")
    Boolean hasCapacity(@PathVariable("id") Long id, @RequestParam("currentEnrolled") int currentEnrolled);

    @GetMapping("/courses/internal/{id}/min-points")
    Boolean hasMinimumPoints(@PathVariable("id") Long id, @RequestParam("points") int points);

    @GetMapping("/courses/internal/open-applications")
    List<CourseDto> getCoursesWithOpenApplications();

    @GetMapping("/courses/internal/id")
    Long getCourseIdByName(@RequestParam String name);
}