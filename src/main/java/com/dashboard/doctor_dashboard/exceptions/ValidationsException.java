package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@AllArgsConstructor
public class ValidationsException extends RuntimeException {
    private final List<String>  messages;

    public List<String> getMessages() {
        return messages;
    }

}
