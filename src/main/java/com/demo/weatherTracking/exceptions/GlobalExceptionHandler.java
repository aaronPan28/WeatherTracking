package com.demo.weatherTracking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrors> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiErrors apiErrors = new ApiErrors(409, ex.getMessage(), new Date());
        return new ResponseEntity<>(apiErrors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrors> handleServiceException(ServiceException ex) {
        ApiErrors apiErrors = new ApiErrors(500, ex.getMessage(), new Date());
        return new ResponseEntity<>(apiErrors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrors> handleEntityNotFound(EntityNotFoundException ex) {
        ApiErrors apiErrors = new ApiErrors(404, ex.getMessage(), new Date());
        return new ResponseEntity<>(apiErrors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WeatherProfileValidationException.class)
    public ResponseEntity<ApiErrors> handleWeatherProfileValidationException(WeatherProfileValidationException ex) {
        ApiErrors apiErrors = new ApiErrors(400, ex.getMessage(), new Date());
        return new ResponseEntity<>(apiErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrors> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage()); // Only field name and message
        });
        // make the default error message shorter
        ApiErrors apiErrors = new ApiErrors(400, errors.toString().replace("=", ": "), new Date());
        return new ResponseEntity<>(apiErrors, HttpStatus.BAD_REQUEST);
    }
}
