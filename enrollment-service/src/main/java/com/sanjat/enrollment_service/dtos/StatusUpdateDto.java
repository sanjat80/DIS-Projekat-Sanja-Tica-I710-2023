package com.sanjat.enrollment_service.dtos;

import com.sanjat.enrollment_service.model.Status;

public class StatusUpdateDto {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
