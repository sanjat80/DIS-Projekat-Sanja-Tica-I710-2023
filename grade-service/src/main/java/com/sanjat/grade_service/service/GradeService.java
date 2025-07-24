package com.sanjat.grade_service.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sanjat.grade_service.dtos.GradeDto;
import com.sanjat.grade_service.dtos.GradeUpdateDto;
import com.sanjat.grade_service.dtos.Notification;
import com.sanjat.grade_service.dtos.NotificationType;
import com.sanjat.grade_service.dtos.StatusUpdateDto;
import com.sanjat.grade_service.exception.EntityNotFoundException;
import com.sanjat.grade_service.exception.ServiceUnavailableException;
import com.sanjat.grade_service.model.Enrollment;
import com.sanjat.grade_service.model.Grade;
import com.sanjat.grade_service.model.Status;
import com.sanjat.grade_service.repository.EnrollmentProxy;
import com.sanjat.grade_service.repository.GradeRepository;

import feign.FeignException;

@Service
public class GradeService {

    private final NotificationSender notificationSender;
    private GradeRepository repository;
    private EnrollmentProxy proxy;

    GradeService(NotificationSender notificationSender, GradeRepository repository,
            EnrollmentProxy proxy) {
        this.notificationSender = notificationSender;
        this.repository = repository;
        this.proxy = proxy;
    }

    public Grade giveGrade(GradeDto gradeRequest) {
        Enrollment enrollment = proxy.getEnrollmentById(gradeRequest.getEnrollmentId());
        System.out.println(enrollment.getId());
        if (!enrollment.getStatus().equals(Status.POHADJA)) {
            throw new IllegalStateException(
                    "Nije moguce ocjeniti studenta koji ne pohadja kurs!");
        }
        Grade grade = new Grade();
        grade.setDateRecorded(LocalDate.now());
        grade.setPoints(gradeRequest.getPoints());
        grade.setGrade(calculateGradeFromPoints(gradeRequest.getPoints()));
        grade.setEnrollmentId(gradeRequest.getEnrollmentId());

        if (grade.getGrade() >= 6) {
            proxy.updateEnrollmentStatus(gradeRequest.getEnrollmentId(), new StatusUpdateDto(Status.ZAVRSIO));
        } else {
            proxy.updateEnrollmentStatus(gradeRequest.getEnrollmentId(), new StatusUpdateDto(Status.NIJE_POLOZIO));
        }

        String courseName = proxy.getCourseByEnrollment(gradeRequest.getEnrollmentId());

        Notification notification = new Notification();
        notification.setType(NotificationType.OCJENJEN);
        notification.setEmail(proxy.getStudentEmailByEnrollmentId(gradeRequest.getEnrollmentId()));
        notification.setEnrollmentId(gradeRequest.getEnrollmentId());
        notification.setCourseName(courseName);
        notification.setMessage("Objavljena je Vasa ocjena na kursu");

        System.out.println("Slanje notifikacije studentu: " + notification.getEmail());
        System.out.println("Poruka: " + notification.toString());
        notificationSender.sendNotification(notification);
        return repository.save(grade);
    }

    public Optional<Grade> getById(Long id) {
        return repository.findById(id);
    }

    public void deleteGrade(Long id) {
        repository.deleteById(id);
    }

    public Grade updateGrade(Long gradeId, GradeUpdateDto newGrade) {
        Grade grade = repository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("Grade with id " + gradeId + " not found"));

        grade.setPoints(newGrade.getPoints());
        grade.setGrade(calculateGradeFromPoints(newGrade.getPoints()));

        return repository.save(grade);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        try {
            List<Enrollment> enrollments = Optional.ofNullable(proxy.getEnrollmentsForCourse(courseId))
                    .orElse(Collections.emptyList());

            List<Long> enrollmentIDs = enrollments.stream()
                    .filter(Objects::nonNull)
                    .map(Enrollment::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (enrollmentIDs.isEmpty()) {
                return Collections.emptyList();
            }

            return repository.findByEnrollmentIdIn(enrollmentIDs);

        } catch (FeignException e) {
            System.out.println("Feign error while getting enrollments for course ");
            throw new ServiceUnavailableException("Enrollment service unavailable");
        } catch (Exception e) {
            System.out.println("Unexpected error while getting grades for course");
            throw new RuntimeException("Failed to get grades", e);
        }
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        List<Enrollment> enrollments = Optional.ofNullable(proxy.getEnrollmentsByStudentId(studentId))
                .orElse(Collections.emptyList());

        System.out.println("All enrollments for student " + studentId + ":");
        enrollments.forEach(e -> System.out.println("ID: " + e.getId() + ", Status: " + e.getStatus()));

        List<Long> enrollmentIDs = enrollments.stream()
                .filter(Objects::nonNull)
                .filter(e -> e.getStatus() == Status.POHADJA) // ili e.getStatus().equals(Status.POHADJA) ako nije enum
                .map(Enrollment::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("Filtered enrollment IDs with status POHADJA: " + enrollmentIDs);

        if (enrollmentIDs.isEmpty()) {
            System.out.println("No enrollments found with status POHADJA.");
            return Collections.emptyList();
        }

        List<Grade> grades = repository.findByEnrollmentIdIn(enrollmentIDs);

        System.out.println("Grades found:");
        grades.forEach(g -> System.out.println(g));

        return grades;
    }

    public List<Grade> getAllGrades() {
        return repository.findAll();
    }

    public int calculateGradeFromPoints(Double points) {
        if (points == null)
            return 5;
        if (points >= 91)
            return 10;
        if (points >= 81)
            return 9;
        if (points >= 71)
            return 8;
        if (points >= 61)
            return 7;
        if (points >= 51)
            return 6;
        return 5;
    }
}
