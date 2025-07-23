package com.sanjat.enrollment_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.sanjat.enrollment_service.dtos.ApplicationDto;
import com.sanjat.enrollment_service.dtos.CourseDto;
import com.sanjat.enrollment_service.dtos.Notification;
import com.sanjat.enrollment_service.dtos.UserDto;
import com.sanjat.enrollment_service.exception.ApplicationFailedException;
import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.model.Enrollment;
import com.sanjat.enrollment_service.model.Status;
import com.sanjat.enrollment_service.repository.ApplicationRepository;
import com.sanjat.enrollment_service.repository.CourseProxy;
import com.sanjat.enrollment_service.repository.EnrollmentRepository;
import com.sanjat.enrollment_service.repository.UserProxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EnrollmentServiceTest {
    private CourseDto courseDto;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @MockBean
    private CourseProxy courseProxy;

    @MockBean
    private UserProxy userProxy;

    @MockBean
    private NotificationSender notificationSender;

    private final Long courseId = 1L;
    private final String courseName = "Java Basics";
    private final String studentEmail = "student@example.com";

    @BeforeEach
    void setupMocks() {
        courseDto = new CourseDto();
        courseDto.setId(courseId);
        courseDto.setName(courseName);
        courseDto.setApplicationStart(LocalDate.of(2025, 07, 01));
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 15));
        courseDto.setCapacity(2);
        courseDto.setMinPoints(50);

        when(courseProxy.getCourseIdByName(courseName)).thenReturn(courseId);
        when(courseProxy.getCourseById(courseId)).thenReturn(courseDto);

        UserDto userDto = new UserDto();
        userDto.setId(100L);
        userDto.setEmail(studentEmail);

        when(userProxy.getUserByEmail(studentEmail)).thenReturn(userDto);
        when(userProxy.getUserById(userDto.getId())).thenReturn(userDto);
    }

    @Test
    void testApplyForCourse_Success() {
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 30));
        courseDto.setCapacity(10);
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCourseName(courseName);
        applicationDto.setEntranceExamPoints(70);
        applicationDto.setName("Marko");
        applicationDto.setSurname("Markovic");

        Application application = enrollmentService.applyForCourse(applicationDto, studentEmail);

        assertThat(application).isNotNull();
        assertThat(application.getStatus()).isEqualTo(Status.ODOBRENA_PRIJAVA);
        assertThat(application.getEmail()).isEqualTo(studentEmail);

        Optional<Application> savedApp = applicationRepository.findById(application.getId());
        assertThat(savedApp).isPresent();
        assertThat(savedApp.get().getStatus()).isEqualTo(Status.ODOBRENA_PRIJAVA);
    }

    @Test
    void testApplyForCourse_FailDueToPoints() {
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 30));

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCourseName(courseName);
        applicationDto.setEntranceExamPoints(30);
        applicationDto.setName("Janko");
        applicationDto.setSurname("Jankovic");

        assertThatThrownBy(() -> enrollmentService.applyForCourse(applicationDto, studentEmail))
                .isInstanceOf(ApplicationFailedException.class)
                .hasMessageContaining("Nedovoljan broj poena!");
    }

    @Test
    void testApplyForCourse_FailDueToCapacity() {
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 30));

        Enrollment e1 = new Enrollment();
        e1.setCourseId(courseId);
        e1.setStudentId(101L);
        e1.setEnrollmentDate(LocalDate.now());
        e1.setStatus(Status.POHADJA);
        enrollmentRepository.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setCourseId(courseId);
        e2.setStudentId(102L);
        e2.setEnrollmentDate(LocalDate.now());
        e2.setStatus(Status.POHADJA);
        enrollmentRepository.save(e2);

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCourseName(courseName);
        applicationDto.setEntranceExamPoints(80);
        applicationDto.setName("Ana");
        applicationDto.setSurname("Anic");

        assertThatThrownBy(() -> enrollmentService.applyForCourse(applicationDto, studentEmail))
                .isInstanceOf(ApplicationFailedException.class)
                .hasMessageContaining("Nema slobodnih mjesta na kursu!");

    }

    @Test
    void testApplyForCourse_FailDueToApplicationPeriod() {
        courseDto.setCapacity(10);
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCourseName(courseName);
        applicationDto.setEntranceExamPoints(80);
        applicationDto.setName("Petar");
        applicationDto.setSurname("Petrovic");

        assertThatThrownBy(() -> enrollmentService.applyForCourse(applicationDto, studentEmail))
                .isInstanceOf(ApplicationFailedException.class)
                .hasMessageContaining("Prijava izvan dozvoljenog termina!");
    }

    @Test
    void testApplyForCourse_FailDueToMaxTimesEnrolled() {
        courseDto.setCapacity(10);
        courseDto.setApplicationStart(LocalDate.of(2025, 07, 05));
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 30));

        Enrollment e1 = new Enrollment();
        e1.setCourseId(courseId);
        e1.setStudentId(100L);
        e1.setEnrollmentDate(LocalDate.now().minusDays(10));
        e1.setStatus(Status.POHADJA);
        enrollmentRepository.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setCourseId(courseId);
        e2.setStudentId(100L);
        e2.setEnrollmentDate(LocalDate.now().minusDays(5));
        e2.setStatus(Status.POHADJA);
        enrollmentRepository.save(e2);

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCourseName(courseName);
        applicationDto.setEntranceExamPoints(80);
        applicationDto.setName("Marko");
        applicationDto.setSurname("Markovic");

        assertThatThrownBy(() -> enrollmentService.applyForCourse(applicationDto, studentEmail))
                .isInstanceOf(ApplicationFailedException.class)
                .hasMessageContaining("Student ne može upisati isti kurs više od 2 puta!");
    }

    @Test
    void testEnrollStudent_Success() {
        courseDto.setApplicationEnd(LocalDate.of(2025, 07, 30));

        Application application = new Application();
        application.setId(1L);
        application.setCourseName(courseName);
        application.setEmail(studentEmail);
        application.setStatus(Status.ODOBRENA_PRIJAVA);
        application.setEntranceExamPoints(80);
        application.setApplicationDate(LocalDate.now());
        application.setName("Marko");
        application.setSurname("Markovic");
        applicationRepository.save(application);

        enrollmentRepository.deleteAll();

        enrollmentService.enrollStudent(application.getId());

        var enrollments = enrollmentRepository.findByStudentId(100L);
        assertThat(enrollments).isNotEmpty();
        Enrollment enrollment = enrollments.get(0);
        assertThat(enrollment.getCourseId()).isEqualTo(courseId);
        assertThat(enrollment.getStatus()).isEqualTo(Status.POHADJA);
        assertThat(enrollment.getNumberOfCourseAttempts()).isEqualTo(1);

        verify(notificationSender, times(1)).sendNotification(any(Notification.class));
    }

    @Test
    void testUpdateStatus() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(100L);
        enrollment.setCourseId(courseId);
        enrollment.setStatus(Status.POHADJA);
        enrollmentRepository.save(enrollment);

        enrollmentService.updateStatus(enrollment.getId(), Status.ZAVRSIO);

        Enrollment updated = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Status.ZAVRSIO);
    }

    @Test
    void testDeleteEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(100L);
        enrollment.setCourseId(courseId);
        enrollmentRepository.save(enrollment);

        enrollmentService.deleteEnrollment(enrollment.getId());

        assertThat(enrollmentRepository.findById(enrollment.getId())).isEmpty();
    }
}
