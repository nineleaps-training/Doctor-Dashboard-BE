package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.Util.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    //     handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorStatus(Constants.FAIL);
        errorMessage.setErrorData(errorDetails);
        log.error("ResourceNotFoundException"+exception.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorMessage> handleAPIException(APIException exception,
                                                           WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("APIException"+exception.getMessage());

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ErrorMessage> handleLoginException(GoogleLoginException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("GoogleLoginException"+exception.getMessage());

        errorMessage.setErrorData(exception);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    //     global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("Exception"+exception.getMessage());

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ValidationsException.class)
    public ResponseEntity<ErrorMessage> processException(final ValidationsException exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("ValidationsException" +exception.getMessage());
        var errorDetails = new ValidationsSchema(new Date(), exception.getMessages(), request.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);


    }

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleMyCustomException(MyCustomException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("MyCustomException"+exception.getMessage());

        errorMessage.setErrorData(exception.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReportNotFound.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleReportNotFoundException(ReportNotFound exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("ReportNotFound"+exception.getMessage());


        errorMessage.setErrorData(exception.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> validation(final ConstraintViolationException exception, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("ConstraintViolationException"+exception.getMessage());
        List<String> newList = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.toList());

        var errorDetails = new ErrorDetails(new Date(), newList.toString(), webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ErrorMessage> invalidDateException(InvalidDate exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        log.error("InvalidDate"+exception.getMessage());
        errorMessage.setErrorData(exception.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
