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

    private List<TaskDto> taskDtoList;
}
