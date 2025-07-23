package com.sanjat.enrollment_service.init;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.model.Enrollment;
import com.sanjat.enrollment_service.model.Status;
import com.sanjat.enrollment_service.repository.ApplicationRepository;
import com.sanjat.enrollment_service.repository.EnrollmentRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ApplicationRepository applicationRepository;

    private final EnrollmentRepository enrollmentRepository;

    public DataInitializer(EnrollmentRepository enrollmentRepository, ApplicationRepository applicationRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (enrollmentRepository.count() == 0) {
            Enrollment enrollment = new Enrollment();
            enrollment.setCourseId(1L);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setNumberOfCourseAttempts(1);
            enrollment.setStatus(Status.POHADJA);
            enrollment.setStudentId(3L);
            enrollmentRepository.save(enrollment);

            Enrollment enrollment3 = new Enrollment();
            enrollment3.setCourseId(2L);
            enrollment3.setEnrollmentDate(LocalDate.now());
            enrollment3.setNumberOfCourseAttempts(1);
            enrollment3.setStatus(Status.POHADJA);
            enrollment3.setStudentId(3L);
            enrollmentRepository.save(enrollment3);

            Enrollment enrollment2 = new Enrollment();
            enrollment2.setCourseId(1L);
            enrollment2.setEnrollmentDate(LocalDate.now());
            enrollment2.setNumberOfCourseAttempts(1);
            enrollment2.setStatus(Status.POHADJA);
            enrollment2.setStudentId(3L);
            enrollmentRepository.save(enrollment2);
        }

        if (applicationRepository.count() == 0) {
            Application application = new Application();
            application.setName("Sanja");
            application.setSurname("Tica");
            application.setCourseName("Fizika 1");
            application.setApplicationDate(LocalDate.now());
            application.setEmail("student@email.com");
            application.setStatus(Status.OBRADA_PRIJAVE);
            applicationRepository.save(application);
        }
    }
}
