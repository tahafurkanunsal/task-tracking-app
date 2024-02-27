package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompanyDto {

    private Long id;

    private String companyName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private boolean isActivate;

    private Long companyOwnerId;

    private String companyOwnerName;

    private List<UserDto> userDtos;

    private List<ProjectInfoDto> projectInfoDtos;
}