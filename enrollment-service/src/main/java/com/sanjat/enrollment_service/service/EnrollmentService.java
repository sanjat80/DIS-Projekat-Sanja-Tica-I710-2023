package com.sanjat.enrollment_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sanjat.enrollment_service.dtos.ApplicationDto;
import com.sanjat.enrollment_service.dtos.CourseDto;
import com.sanjat.enrollment_service.dtos.EnrollmentDto;
import com.sanjat.enrollment_service.dtos.Notification;
import com.sanjat.enrollment_service.dtos.NotificationType;
import com.sanjat.enrollment_service.dtos.UserDto;
import com.sanjat.enrollment_service.exception.ApplicationFailedException;
import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.model.Enrollment;
import com.sanjat.enrollment_service.model.Status;
import com.sanjat.enrollment_service.repository.ApplicationRepository;
import com.sanjat.enrollment_service.repository.CourseProxy;
import com.sanjat.enrollment_service.repository.EnrollmentRepository;
import com.sanjat.enrollment_service.repository.UserProxy;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnrollmentService {

    private final NotificationSender notificationSender;

    private final EnrollmentRepository repository;

    private final ApplicationRepository appRepository;

    private CourseProxy courseProxy;

    UserProxy userProxy;

    public EnrollmentService(EnrollmentRepository repository, CourseProxy courseProxy,
            UserProxy userProxy,
            ApplicationRepository appRepository,
            NotificationSender notificationSender) {
        this.repository = repository;
        this.courseProxy = courseProxy;
        this.userProxy = userProxy;
        this.appRepository = appRepository;
        this.notificationSender = notificationSender;
    }

    public Application applyForCourse(ApplicationDto application, String email) {
        Long courseId = courseProxy.getCourseIdByName(application.getCourseName());
        CourseDto course = courseProxy.getCourseById(courseId);

        LocalDate now = LocalDate.now();
        String rejectionReason = null;

        if (now.isBefore(course.getApplicationStart()) || now.isAfter(course.getApplicationEnd())) {
            rejectionReason = "Prijava izvan dozvoljenog termina!";
        } else if (application.getEntranceExamPoints() < course.getMinPoints()) {
            rejectionReason = "Nedovoljan broj poena!";
        } else {
            int enrolledCount = repository.countByCourseId(courseId);
            if (enrolledCount >= course.getCapacity()) {
                rejectionReason = "Nema slobodnih mjesta na kursu!";
            }
        }

        var student = userProxy.getUserByEmail(email);
        int timesEnrolled = repository.countByStudentIdAndCourseId(student.getId(), courseId);
        if (rejectionReason == null && timesEnrolled >= 2) {
            rejectionReason = "Student ne može upisati isti kurs više od 2 puta!";
        }

        Application savedApplication = new Application();
        savedApplication.setApplicationDate(now);
        savedApplication.setCourseName(application.getCourseName());
        savedApplication.setEntranceExamPoints(application.getEntranceExamPoints());
        savedApplication.setName(application.getName());
        savedApplication.setSurname(application.getSurname());
        savedApplication.setEmail(email);
        savedApplication.setStatus(rejectionReason == null ? Status.ODOBRENA_PRIJAVA : Status.ODBIJENA_PRIJAVA);

        appRepository.save(savedApplication);

        if (rejectionReason != null) {
            throw new ApplicationFailedException(rejectionReason);
        }

        return savedApplication;
    }

    public List<Enrollment> GetAllEnrolments() {
        return repository.findAll();
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return repository.findByCourseId(courseId);
    }

    public void unenrollStudent(Long studentId, Long courseId) {
        repository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    public void updateStatus(Long enrollmentId, Status status) {
        Enrollment enrollment = repository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Prijava nije pronadjena"));
        enrollment.setStatus(status);
        repository.save(enrollment);
    }

    public void enrollStudent(Long applicationId) {
        Application app = appRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Prijava nije pronađena"));
        var student = userProxy.getUserByEmail(app.getEmail());
        Long studentId = student.getId();

        Long courseId = courseProxy.getCourseIdByName(app.getCourseName());

        int timesEnrolled = repository.countByStudentIdAndCourseId(studentId, courseId);
        String courseName = app.getCourseName();
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setNumberOfCourseAttempts(timesEnrolled + 1);
        if (enrollment.getNumberOfCourseAttempts() == 2) {
            enrollment.setStatus(Status.OBNAVLJA);
        } else {
            enrollment.setStatus(Status.POHADJA);
        }
        repository.save(enrollment);

        Notification notification = new Notification();
        notification.setEmail(app.getEmail());
        notification.setEnrollmentId(enrollment.getId());
        notification.setCourseName(courseName);
        notification.setMessage("Upisani ste na kurs");
        notification.setType(NotificationType.USPJESNO_UPISAN);
        notificationSender.sendNotification(notification);
    }

    public List<EnrollmentDto> getEnrollmentsForStudent(Long studentId) {
        List<Enrollment> enrollments = repository.findByStudentId(studentId);
        return enrollments.stream()
                .map(enrollment -> {
                    EnrollmentDto dto = new EnrollmentDto();
                    dto.setStatus(enrollment.getStatus());
                    dto.setEnrollmentDate(enrollment.getEnrollmentDate());

                    CourseDto course = courseProxy.getCourseById(enrollment.getCourseId());
                    dto.setCourse(course.getName());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public void deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = repository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Nije pronadjen upis sa datim id-em: " + enrollmentId));
        repository.delete(enrollment);
    }

    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return repository.findByCourseId(courseId);
    }

    public Enrollment getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return repository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("Upis na kurs za datog studenta nije pronadjen!"));
    }

    public Enrollment getEnrollmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Upis sa id-em:  " + id + " nije pronadjen."));
    }

    public String getStudentEmailByEnrollmentId(Long enrollmentId) {
        Long studentId = repository.findStudentIdByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Student za dati enrollment id nije pronadjen!"));

        UserDto user = userProxy.getUserById(studentId);
        return user.getEmail();
    }

    public String getCourseByEnrollment(Long enrollmentId) {
        Enrollment enrollment = repository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Upis za dati id nije pronadjen!"));
        CourseDto course = courseProxy.getCourseById(enrollment.getCourseId());
        return course.getName();
    }
}
