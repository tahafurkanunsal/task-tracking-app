package com.tfunsal.TaskManagement.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String name;

    private String surname;

    private String email;

    private String password;
}