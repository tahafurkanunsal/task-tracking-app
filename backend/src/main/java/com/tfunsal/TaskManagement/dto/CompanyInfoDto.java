package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyInfoDto {

    private Long id;

    private String companyName;

    private boolean isActivate;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long companyOwnerId;

    private String companyOwnerName;
}