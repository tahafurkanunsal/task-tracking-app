package com.tfunsal.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedProjectAccessException extends RuntimeException{
    public UnauthorizedProjectAccessException(String message) {
        super(message);
    }
}
