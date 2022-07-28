package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.dtos.ErrorMessage;
import com.dashboard.doctor_dashboard.util.wrappers.ErrorDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CommonExceptionHandlerTest {

    @InjectMocks
    CommonExceptionHandler globalExceptionHandler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void handleAPIException() {
        WebRequest request = mock(WebRequest.class);
        ErrorDetails details = new ErrorDetails(new Date(), "test for API exception", request.getDescription(false));
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleAPIException(new APIException("test for API exception"), request);
        Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        Assertions.assertEquals(details.getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());
    }

    @Test
    void handleLoginException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for google login exception", null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleLoginException(new GoogleLoginException("test for google login exception"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals(details.getMessage(), Objects.requireNonNull(response.getBody()).getErrorData());
    }

    @Test
    void handleGlobalException() {
        WebRequest request = mock(WebRequest.class);
        ErrorDetails details = new ErrorDetails(new Date(), "test for global exception", request.getDescription(false));
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleGlobalException(new Exception("test for global exception"), request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(details.getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }


    @Test
    void handleResourceNotFoundException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for resource not found exception", null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("test for resource not found exception"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(details.getMessage(), Objects.requireNonNull(response.getBody()).getErrorData());
    }


    @Test
    void invalidDateException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for invalid date exception", null);
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.invalidDateException(new InvalidDate("test for invalid date exception"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals(details.getMessage().getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }

    @Test
    void mailErrorException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for mail error",null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.mailErrorException(new MailErrorException(details.getMessage()));
        Assertions.assertEquals(HttpStatus.FAILED_DEPENDENCY,response.getStatusCode());
        Assertions.assertEquals(details.getMessage().getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }


}