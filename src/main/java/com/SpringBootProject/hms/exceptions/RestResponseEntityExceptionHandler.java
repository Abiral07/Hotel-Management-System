package com.SpringBootProject.hms.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> CustomException(CustomException customException, WebRequest request) {
        return this.createResponseEntity(HttpStatus.NOT_ACCEPTABLE, customException, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        //to extract the default error message from diagnostic information about the errors held in MethodArgumentNotValidException
        Exception exception = new Exception(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return this.createResponseEntity(HttpStatus.BAD_REQUEST, exception, request);
    }

    private ResponseEntity<Object> createResponseEntity(
            HttpStatus httpStatus, Exception ex, WebRequest request) {
        ErrorMessage errorResponse = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus)
                .statusValue(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(true))
                .build();
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }
}

