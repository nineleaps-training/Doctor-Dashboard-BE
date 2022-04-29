package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GoogleLoginException extends RuntimeException {
    private final String message;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
