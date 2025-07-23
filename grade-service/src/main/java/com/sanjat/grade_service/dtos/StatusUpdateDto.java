package com.sanjat.grade_service.dtos;

import com.sanjat.grade_service.model.Status;

public class StatusUpdateDto {
    private Status status;

    public StatusUpdateDto(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
