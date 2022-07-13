package com.dashboard.doctor_dashboard.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDate extends RuntimeException{
    public InvalidDate(String message) {
        super(message);
    }
}
