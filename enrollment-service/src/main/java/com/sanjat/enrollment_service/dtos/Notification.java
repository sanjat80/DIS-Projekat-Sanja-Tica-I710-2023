package com.sanjat.enrollment_service.dtos;

public class Notification {
    private Long enrollmentId;
    private NotificationType type;
    private String email;
    private String message;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

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

    @Override
    public String toString() {
        return "Notification{" +
                "enrollmentId=" + enrollmentId +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
