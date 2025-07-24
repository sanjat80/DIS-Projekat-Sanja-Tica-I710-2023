package com.sanjat.grade_service.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sanjat.grade_service.config.FeignClientConfig;
import com.sanjat.grade_service.dtos.StatusUpdateDto;
import com.sanjat.grade_service.model.Enrollment;

@FeignClient(name = "enrollment-service", configuration = FeignClientConfig.class)
public interface EnrollmentProxy {
    @GetMapping("/enrollments/internal/{id}")
    Enrollment getEnrollmentById(@PathVariable Long id);

    @GetMapping("/enrollments/internal/course/{courseId}")
    List<Enrollment> getEnrollmentsForCourse(@PathVariable Long courseId);

    @GetMapping("/enrollments/internal/students/{studentId}")
    List<Enrollment> getEnrollmentsByStudentId(@PathVariable Long studentId);

    @GetMapping("/enrollments/internal/email/{enrollmentId}")
    String getStudentEmailByEnrollmentId(@PathVariable Long enrollmentId);

    @GetMapping("/enrollments/internal/course/name/{enrollmentId}")
    String getCourseByEnrollment(@PathVariable Long enrollmentId);

    @PutMapping("/enrollments/internal/{id}/status")
    Void updateEnrollmentStatus(@PathVariable("id") Long enrollmentId,
            @RequestBody StatusUpdateDto updateRequest);

}