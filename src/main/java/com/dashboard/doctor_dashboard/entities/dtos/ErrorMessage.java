package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ErrorMessage {
    private String errorStatus;
    private Object errorData;

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }
}
