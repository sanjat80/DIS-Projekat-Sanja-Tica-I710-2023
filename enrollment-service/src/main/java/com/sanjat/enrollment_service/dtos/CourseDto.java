package com.sanjat.enrollment_service.dtos;

import java.time.LocalDate;

public class CourseDto {
    private Long id;
    private String name;
    private int capacity;
    private LocalDate applicationStart;
    private LocalDate applicationEnd;
    private int minPoints;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setApplicationStart(LocalDate applicationStart) {
        this.applicationStart = applicationStart;
    }

    public void setApplicationEnd(LocalDate applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalDate getApplicationStart() {
        return applicationStart;
    }

    public LocalDate getApplicationEnd() {
        return applicationEnd;
    }

    public int getMinPoints() {
        return minPoints;
    }

}
