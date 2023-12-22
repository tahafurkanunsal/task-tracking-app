package com.tfunsal.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedTaskAccessException extends RuntimeException{
    public UnauthorizedTaskAccessException(String message) {
        super(message);
    }
}
