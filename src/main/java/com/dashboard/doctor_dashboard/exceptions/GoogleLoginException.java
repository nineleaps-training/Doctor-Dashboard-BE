package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@AllArgsConstructor
@Getter
public class GoogleLoginException extends RuntimeException {
    private final String message;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
