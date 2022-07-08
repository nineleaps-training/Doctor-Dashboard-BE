package com.dashboard.doctor_dashboard.entities.dtos;

import javax.validation.constraints.Pattern;


public class StatusDto {

    @Pattern(regexp = "^(active|inactive)", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Select from specified status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}