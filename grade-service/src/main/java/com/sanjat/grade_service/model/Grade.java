package com.sanjat.grade_service.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;
    private Long enrollmentId;

    public Grade() {
    }

    public Grade(Long gradeId, Long enrollmentId, LocalDate dateRecorded, double points, int grade) {
        this.gradeId = gradeId;
        this.enrollmentId = enrollmentId;
        this.dateRecorded = dateRecorded;
        this.points = points;
        this.grade = grade;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    private LocalDate dateRecorded;
    private Double points;
    private int grade;

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public void setDateRecorded(LocalDate dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public LocalDate getDateRecorded() {
        return dateRecorded;
    }

    public Double getPoints() {
        return points;
    }

    public int getGrade() {
        return grade;
    }

}
