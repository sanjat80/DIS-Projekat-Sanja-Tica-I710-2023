package com.sanjat.grade_service.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sanjat.grade_service.dtos.StatusUpdateDto;
import com.sanjat.grade_service.model.Enrollment;
import com.sanjat.grade_service.model.Status;

@FeignClient("enrollment-service")
public interface EnrollmentProxy {
    @GetMapping("/enrollments/{id}")
    Enrollment getEnrollmentById(@PathVariable Long id);

    @GetMapping("/enrollments/course/{courseId}")
    List<Enrollment> getEnrollmentsForCourse(@PathVariable Long courseId);

    @GetMapping("/enrollments/students/{studentId}")
    List<Enrollment> getEnrollmentsByStudentId(@PathVariable Long studentId);

    @GetMapping("/enrollments/email/{enrollmentId}")
    String getStudentEmailByEnrollmentId(@PathVariable Long enrollmentId);

    @GetMapping("/enrollments/course/name/{enrollmentId}")
    String getCourseByEnrollment(@PathVariable Long enrollmentId);

    @PutMapping("/enrollments/{id}/status")
    Void updateEnrollmentStatus(@PathVariable("id") Long enrollmentId,
            @RequestBody StatusUpdateDto updateRequest);

}