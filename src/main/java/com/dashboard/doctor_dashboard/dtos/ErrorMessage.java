package com.dashboard.doctor_dashboard.dtos;

import lombok.Data;

@Data
public class ErrorMessage {
    private String errorStatus;
    private Object errorData;
}
