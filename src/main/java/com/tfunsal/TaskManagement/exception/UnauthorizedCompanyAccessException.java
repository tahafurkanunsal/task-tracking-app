package com.tfunsal.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedCompanyAccessException extends RuntimeException {
    public UnauthorizedCompanyAccessException(String message) {
        super(message);
    }
}
