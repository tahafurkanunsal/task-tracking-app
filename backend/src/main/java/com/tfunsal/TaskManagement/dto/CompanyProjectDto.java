package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyProjectDto {

    private String companyName;
    private List<ProjectInfoDto> projectInfoDtos;
}