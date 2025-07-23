package com.sanjat.grade_service.model;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Enrollment {
    private Long id;

    private Long studentId;

    public Enrollment() {
    }

    public Long getId() {
        return id;
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

    public void setNumberOfCourseAttempts(int numberOfCourseAttempts) {
        this.numberOfCourseAttempts = numberOfCourseAttempts;
    }

    public Long getStudentId() {
        return studentId;
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

    public Enrollment(Long id, Long studentId, Long courseId, Status status, LocalDate enrollmentDate,
            int numberOfCourseAttempts) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
        this.numberOfCourseAttempts = numberOfCourseAttempts;
    }

    public int getNumberOfCourseAttempts() {
        return numberOfCourseAttempts;
    }

    private Long courseId;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate enrollmentDate;
    private int numberOfCourseAttempts;
}
