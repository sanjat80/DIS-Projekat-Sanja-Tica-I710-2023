package com.sanjat.course_service.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int durationInClasses;

    private int semester;

    private int espb_Points;
    private String professor;
    private int capacity;
    private double minPoints;
    private LocalDate applicationStart;
    private LocalDate applicationEnd;

    public Long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getMinPoints() {
        return minPoints;
    }

    public LocalDate getApplicationStart() {
        return applicationStart;
    }

    public LocalDate getApplicationEnd() {
        return applicationEnd;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMinPoints(double minPoints) {
        this.minPoints = minPoints;
    }

    public void setApplicationStart(LocalDate applicationStart) {
        this.applicationStart = applicationStart;
    }

    public void setApplicationEnd(LocalDate applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDurationInClasses(int durationInClasses) {
        this.durationInClasses = durationInClasses;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setEspb_Points(int espb_Points) {
        this.espb_Points = espb_Points;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationInClasses() {
        return durationInClasses;
    }

    public int getSemester() {
        return semester;
    }

    public int getEspb_Points() {
        return espb_Points;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
