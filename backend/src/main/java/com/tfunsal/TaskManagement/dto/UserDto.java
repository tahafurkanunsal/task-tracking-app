package com.tfunsal.TaskManagement.dto;

import com.tfunsal.TaskManagement.enums.CompanyRole;
import com.tfunsal.TaskManagement.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String fullName;

    private UserRole role;

    private List<CompanyRole> companyRoles;

    private Long companyId;

    private String companyName;
}