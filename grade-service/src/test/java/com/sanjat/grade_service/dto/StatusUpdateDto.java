package com.sanjat.grade_service.dto;

public class StatusUpdateDto {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public StatusUpdateDto(Status status) {
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
