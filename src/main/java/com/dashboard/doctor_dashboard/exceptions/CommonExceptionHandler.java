package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.entities.dtos.ErrorMessage;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.ErrorDetails;
import com.dashboard.doctor_dashboard.util.wrappers.ValidationsSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Date;


@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

//     handle specific exceptions

    /**
     * This function of service is call whenever API exception is thrown
     * @param exception Custom API exception
     * @param webRequest
     * @return ResponseEntity<ErrorMessage> with status code 405
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorMessage> handleAPIException(APIException exception,
                                                           WebRequest webRequest) {

        log.error("APIException::"+exception.getMessage());

        var errorMessage = new ErrorMessage();

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }
    /**
     * This function of service is call whenever GoogleLogin exception is thrown
     * @param ex Custom GoogleLoginException
     * @return ResponseEntity<ErrorMessage> with status code 401
     */
    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ErrorMessage> handleLoginException(GoogleLoginException ex) {
        var errorMessage = new ErrorMessage();

        log.error("GoogleLoginException::"+ex.getMessage());


        errorMessage.setErrorData(ex.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }
    /**
     * This function of service is call whenever  exception is thrown
     * @param exception
     * @param webRequest
     * @return ResponseEntity<ErrorMessage> with status code 404
     */
    //     global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        var errorMessage = new ErrorMessage();
        log.error("Exception::"+exception.getMessage());

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * This function of service is call whenever API exception is thrown
     * @param ex MethodArgumentNotValid exception
     * @return ResponseEntity<ErrorMessage> with status code 422
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> processException(MethodArgumentNotValidException ex, WebRequest request) {
        var errorMessage = new ErrorMessage();
        log.error("ValidationsException::"+ex.getMessage());


        var errorDetails = new ValidationsSchema(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);


    }
    /**
     * This function of service is call whenever ResourceNotFound exception is thrown
     * @param e Custom ResourceNotFound Exception
     * @return ResponseEntity<ErrorMessage> with status code 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {

        var errorMessage = new ErrorMessage();
        log.error("ResourceNotFound::"+e.getMessage());


        errorMessage.setErrorData(e.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    /**
     * This function of service is call whenever InvalidDate exception is thrown
     * @param e Custom Invalid Date exception
     * @return ResponseEntity<ErrorMessage> with status code 409
     */
    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ErrorMessage> invalidDateException(InvalidDate e) {
        var errorMessage = new ErrorMessage();
        log.error("InvalidDate::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }
    /**
     * This function of service is call whenever Mail Error exception is thrown
     * @param e Custom Mail Error Exception
     * @return ResponseEntity<ErrorMessage> with status code 424
     */
    @ExceptionHandler(MailErrorException.class)
    public ResponseEntity<ErrorMessage> mailErrorException(MailErrorException e) {
        var errorMessage = new ErrorMessage();
        log.error("Mail Error::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.FAILED_DEPENDENCY);
    }
}
