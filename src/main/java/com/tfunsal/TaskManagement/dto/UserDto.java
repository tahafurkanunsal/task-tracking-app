package com.tfunsal.TaskManagement.dto;

import com.tfunsal.TaskManagement.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String fullName;

    private UserRole role;
}
