package com.sanjat.enrollment_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sanjat.enrollment_service.dtos.ApplicationDto;
import com.sanjat.enrollment_service.dtos.EnrollmentDto;
import com.sanjat.enrollment_service.exception.ApplicationFailedException;
import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.model.Enrollment;
import com.sanjat.enrollment_service.model.Status;
import com.sanjat.enrollment_service.service.ApplicationService;
import com.sanjat.enrollment_service.service.EnrollmentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private EnrollmentService service;
    private ApplicationService appService;

    public EnrollmentController(EnrollmentService service,
            ApplicationService appService) {
        this.service = service;
        this.appService = appService;
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplication() {
        return ResponseEntity.ok(appService.getAllAplications());
    }

    // this method is being used by grade-service
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEnrollmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(service.GetAllEnrolments());
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> apply(@RequestBody ApplicationDto applicationRequest) {
        try {
            Application application = service.applyForCourse(applicationRequest, applicationRequest.getEmail());
            return ResponseEntity.ok(application);
        } catch (ApplicationFailedException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException ex) {
            throw new ApplicationFailedException(ex.getMessage());
        }
    }

    @PostMapping("/enroll/{applicationId}")
    public ResponseEntity<String> enrollStudent(@PathVariable Long applicationId) {
        try {
            service.enrollStudent(applicationId);
            return ResponseEntity.ok("Student je uspjesno upisan na kurs.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollments(
            @PathVariable Long studentId) {
        List<EnrollmentDto> dtos = service.getEnrollmentsForStudent(studentId);
        return ResponseEntity.ok(dtos);
    }

    // this method is being used by grade-service
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getEnrollmentsByStudentId(studentId));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long enrollmentId) {
        service.deleteEnrollment(enrollmentId);
        return ResponseEntity.noContent().build();
    }

    // this method is being used by enrollment-service
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsForCourse(@PathVariable Long courseId) {
        List<Enrollment> enrollments = service.getEnrollmentsByCourseId(courseId);
        if (enrollments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/enrollment/{studentId}/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(@PathVariable Long studentId,
            @PathVariable Long courseId) {
        Enrollment enrollment = service.getEnrollmentByStudentAndCourse(studentId, courseId);
        if (enrollment == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enrollment);

    }

    // this method is being used by grade-service
    @GetMapping("/email/{enrollmentId}")
    public ResponseEntity<String> getStudentEmailByEnrollmentId(@PathVariable Long enrollmentId) {
        String email = service.getStudentEmailByEnrollmentId(enrollmentId);
        if (email == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(email);
    }

    // this method is being used by grade-service
    @GetMapping("/course/name/{enrollmentId}")
    public ResponseEntity<String> getCourseByEnrollment(@PathVariable Long enrollmentId) {
        String course = service.getCourseByEnrollment(enrollmentId);
        if (course == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateEnrollmentStatus(@PathVariable("id") Long enrollmentId,
            @RequestParam("status") Status status) {
        service.updateStatus(enrollmentId, status);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}