package com.india.taskmanager.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice

public class GlobalExceptionHandler {
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse>
handleResourceNotFoundException(ResourceNotFoundException ex){
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
}

@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse>
    handleDuplicateResourceException(DuplicateResourceException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
    }
@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
public ResponseEntity<ErrorResponse>
handleEnumError(org.springframework.http.converter.HttpMessageNotReadableException ex) {
    String message = "Invalid request. Please check your input values";
    if(ex.getMessage().contains("Priority")){
        message = "Invalid value for Priority (LOW, HIGH, and MEDIUM allowed)";
    }

    return new ResponseEntity<>(new ErrorResponse(message, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(
        org.springframework.web.bind.MethodArgumentNotValidException ex) {
 
    String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("Validation error");
 
    return new ResponseEntity<>(
            new ErrorResponse(message, HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
    );
}
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse>
handleGeneralException(Exception ex){
    return new ResponseEntity<>(new ErrorResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
}
}
