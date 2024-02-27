package com.tfunsal.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotInCompanyException extends RuntimeException {
    public UserNotInCompanyException(String message) {
        super(message);
    }
}
