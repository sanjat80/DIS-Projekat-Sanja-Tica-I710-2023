package com.sanjat.notification_service.model;

public class Notification {
    private Long enrollmentId;
    private NotificationType type;
    private String email;
    private String message;

    public String getCourseName() {
        return courseName;
    }

    private String courseName;

    public Notification() {
    }

    public Notification(Long enrollmentId, NotificationType type, String message) {
        this.enrollmentId = enrollmentId;
        this.type = type;
        this.message = message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
