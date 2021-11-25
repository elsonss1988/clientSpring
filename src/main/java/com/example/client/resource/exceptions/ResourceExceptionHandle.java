package com.example.client.resource.exceptions;

import com.example.client.services.exceptions.DataBaseException;
import com.example.client.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandle {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request){
        StandardError err = new StandardError();
        HttpStatus statusHttp = HttpStatus.NOT_FOUND;
        err.setTimestamp(Instant.now());
        err.setStatus(statusHttp.value());
        err.setError("Resource Not Found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(statusHttp)
                .body(err);
    }


    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBaseException(
            DataBaseException e,
            HttpServletRequest request){
        StandardError err = new StandardError();
        HttpStatus statusHttp= HttpStatus.BAD_REQUEST;
        err.setTimestamp(Instant.now());
        err.setStatus(statusHttp.value());
        err.setMessage(e.getMessage());
        err.setError("Database Exception");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(statusHttp).body(err);
    }
}
