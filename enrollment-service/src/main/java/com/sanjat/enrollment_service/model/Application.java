package com.sanjat.enrollment_service.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    private String courseName;
    private double entranceExamPoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    private String name;
    private String surname;
    private LocalDate applicationDate;

    private Status status;

    public void setEntranceExamPoints(double entranceExamPoints) {
        this.entranceExamPoints = entranceExamPoints;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getEntranceExamPoints() {
        return entranceExamPoints;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

}
