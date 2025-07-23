package com.sanjat.enrollment_service.dtos;

import jakarta.validation.constraints.NotBlank;

public class ApplicationDto {
    @NotBlank(message = "Naziv kursa na koji se prijavljujete je obavezan!")
    private String courseName;
    @NotBlank(message = "Ime ucesnika kursa je obavezno!")
    private String name;
    @NotBlank(message = "Prezime ucesnika je obavezno!")
    private String surname;
    @NotBlank(message = "Obavezno je navesti broj poena sa ulaznog testa!")
    private double entranceExamPoints;
    @NotBlank(message = "Obavezno je navesti email!")
    private String email;

    public String getCourseName() {
        return courseName;
    }

    public String getName() {
        return name;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEntranceExamPoints(double entranceExamPoints) {
        this.entranceExamPoints = entranceExamPoints;
    }

    public String getSurname() {
        return surname;
    }

    public double getEntranceExamPoints() {
        return entranceExamPoints;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}