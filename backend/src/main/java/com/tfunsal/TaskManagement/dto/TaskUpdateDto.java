package com.tfunsal.TaskManagement.dto;

import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TaskUpdateDto {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskTag tag;

    private LocalDateTime modifiedDate;

    private LocalDateTime dueDate;

    private List<Long> userIds = new ArrayList<>();

    private Long projectId;

    private String projectName;
}