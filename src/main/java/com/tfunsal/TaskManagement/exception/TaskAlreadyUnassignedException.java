package com.tfunsal.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskAlreadyUnassignedException extends RuntimeException {
    public TaskAlreadyUnassignedException(String message) {
        super(message);
    }
}
