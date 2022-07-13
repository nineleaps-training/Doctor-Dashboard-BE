package com.dashboard.doctor_dashboard.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
