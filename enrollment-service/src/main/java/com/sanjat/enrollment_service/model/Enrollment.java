package com.sanjat.enrollment_service.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long courseId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setNumberOfCourseAttempts(int numberOfCourseAttempts) {
        this.numberOfCourseAttempts = numberOfCourseAttempts;
    }

    private LocalDate enrollmentDate;
    private int numberOfCourseAttempts;

    public Enrollment() {
    }

    public int getNumberOfCourseAttempts() {
        return numberOfCourseAttempts;
    }

    public Enrollment(Long studentId, Long courseId, LocalDate enrollmentDate, Status status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
}
