package com.sanjat.enrollment_service.dtos;

import java.time.LocalDate;

import com.sanjat.enrollment_service.model.Status;

public class EnrollmentDto {
    private Status status;
    private LocalDate enrollmentDate;

    public String getCourse() {
        return course;
    }

    private String course;

    public Status getStatus() {
        return status;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
