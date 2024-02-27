package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long companyId;

    private String companyName;

    private List<TaskDto> taskDtoList;
}