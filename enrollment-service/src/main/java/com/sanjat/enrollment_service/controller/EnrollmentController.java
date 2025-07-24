package com.sanjat.enrollment_service.controller;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanjat.enrollment_service.dtos.ApplicationDto;
import com.sanjat.enrollment_service.dtos.EnrollmentDto;
import com.sanjat.enrollment_service.dtos.StatusUpdateDto;
import com.sanjat.enrollment_service.exception.ApplicationFailedException;
import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.model.Enrollment;
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
    public ResponseEntity<?> getAllApplication() {
        ResponseEntity<?> response;
        try {
            List<Application> applications = appService.getAllAplications();
            response = ResponseEntity.ok(applications);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju svih prijava: " + e.getMessage());
        }
        return response;
    }

    // this method is being used by grade-service
    @GetMapping("/internal/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        ResponseEntity<?> response;
        try {
            Enrollment enrollment = service.getEnrollmentById(id);
            if (enrollment == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Upis na kurs sa id-em: " + id + " nije pronadjen.");
            } else {
                response = ResponseEntity.ok(enrollment);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju upisa na kurs:  " + e.getMessage());
        }
        return response;
    }

    @GetMapping
    public ResponseEntity<?> getAllEnrollments() {
        ResponseEntity<?> response;
        try {
            List<Enrollment> enrollments = service.GetAllEnrolments();
            response = ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju svih upisa na kurs: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody ApplicationDto applicationRequest) {
        ResponseEntity<?> response;
        try {
            Application application = service.applyForCourse(applicationRequest, applicationRequest.getEmail());
            response = ResponseEntity.ok(application);
        } catch (ApplicationFailedException ex) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        // } catch (RuntimeException ex) {
        // response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body("Prijava na kurs neuspjesna: " + ex.getMessage());
        // }
        return response;
    }

    @PostMapping("/enroll/{applicationId}")
    public ResponseEntity<?> enrollStudent(@PathVariable Long applicationId) {
        ResponseEntity<?> response;
        try {
            service.enrollStudent(applicationId);
            response = ResponseEntity.ok("Student je uspješno upisan na kurs.");
        } catch (Exception ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska prilikom upisivanja studenta: " + ex.getMessage());
        }
        return response;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentEnrollments(@PathVariable Long studentId) {
        ResponseEntity<?> response;
        try {
            List<EnrollmentDto> dtos = service.getEnrollmentsForStudent(studentId);
            response = ResponseEntity.ok(dtos);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju upisa na kurs za studenta: " + studentId + "Greska: "
                            + e.getMessage());
        }
        return response;
    }

    @GetMapping("/internal/students/{studentId}")
    public ResponseEntity<?> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        ResponseEntity<?> response;
        try {
            List<Enrollment> enrollments = service.getEnrollmentsByStudentId(studentId);
            response = ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju upisa na kurs za studenta sa id-em: " + studentId + ". Greska: "
                            + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long enrollmentId) {
        ResponseEntity<?> response;
        try {
            service.deleteEnrollment(enrollmentId);
            response = ResponseEntity.noContent().build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri brisanju upisa na kurs: " + e.getMessage());
        }
        return response;
    }

    // this method is being used by grade-service
    @GetMapping("/internal/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsForCourse(@PathVariable Long courseId) {
        ResponseEntity<?> response;
        try {
            List<Enrollment> enrollments = service.getEnrollmentsByCourseId(courseId);
            if (enrollments.isEmpty()) {
                response = ResponseEntity.noContent().build();
            } else {
                response = ResponseEntity.ok(enrollments);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri brisanju upisa na kurs sa id-em: " + courseId + ". Greska: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/enrollment/{studentId}/{courseId}")
    public ResponseEntity<?> getEnrollmentByStudentAndCourse(@PathVariable Long studentId,
            @PathVariable Long courseId) {
        ResponseEntity<?> response;
        try {
            Enrollment enrollment = service.getEnrollmentByStudentAndCourse(studentId, courseId);
            if (enrollment == null) {
                response = ResponseEntity.noContent().build();
            } else {
                response = ResponseEntity.ok(enrollment);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju upisa po studentu i kursu: " + e.getMessage());
        }
        return response;
    }

    // this method is being used by grade-service
    @GetMapping("/internal/email/{enrollmentId}")
    public ResponseEntity<?> getStudentEmailByEnrollmentId(@PathVariable Long enrollmentId) {
        ResponseEntity<?> response;
        try {
            String email = service.getStudentEmailByEnrollmentId(enrollmentId);
            if (email == null) {
                response = ResponseEntity.noContent().build();
            } else {
                response = ResponseEntity.ok(email);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju studentskog mejla: " + e.getMessage());
        }
        return response;
    }

    // this method is being used by grade-service
    @GetMapping("/internal/course/name/{enrollmentId}")
    public ResponseEntity<?> getCourseByEnrollment(@PathVariable Long enrollmentId) {
        ResponseEntity<?> response;
        try {
            String course = service.getCourseByEnrollment(enrollmentId);
            if (course == null) {
                response = ResponseEntity.noContent().build();
            } else {
                response = ResponseEntity.ok(course);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju kursa na koji se odnosi upis sa id-em: " + enrollmentId + ". Greska: "
                            + e.getMessage());
        }
        return response;
    }

    @PutMapping("/internal/{id}/status")
    public ResponseEntity<?> updateEnrollmentStatus(@PathVariable("id") Long enrollmentId,
            @RequestBody StatusUpdateDto updateRequest) {
        ResponseEntity<?> response;
        try {
            service.updateStatus(enrollmentId, updateRequest.getStatus());
            response = ResponseEntity.noContent().build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri azuriranju upisa: " + e.getMessage());
        }
        return response;
    }
}