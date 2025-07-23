package com.sanjat.grade_service.dtos;

public class GradeDto {
    private Long enrollmentId;
    private double points;

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getPoints() {
        return points;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

}
