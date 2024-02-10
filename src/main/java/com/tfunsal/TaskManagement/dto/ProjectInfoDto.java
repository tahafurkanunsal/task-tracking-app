package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectInfoDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdDate;
}