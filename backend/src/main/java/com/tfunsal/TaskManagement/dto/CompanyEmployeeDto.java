package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyEmployeeDto {

    private String companyName;
    private List<UserDto> userDtos;
}