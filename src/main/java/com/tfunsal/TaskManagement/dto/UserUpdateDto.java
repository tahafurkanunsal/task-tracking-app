package com.tfunsal.TaskManagement.dto;

import com.tfunsal.TaskManagement.enums.UserRole;
import lombok.Data;

@Data
public class UserUpdateDto {

    private Long id;

    private String email;

    private String name;

    private String surname;

    private UserRole role;
}