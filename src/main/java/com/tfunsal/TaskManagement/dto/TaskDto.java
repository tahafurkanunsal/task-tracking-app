package com.tfunsal.TaskManagement.dto;

import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskTag tag;

    private LocalDateTime createdDate;

    private LocalDateTime dueDate;

    private Long userId;

    List<CommentDto> comments = new ArrayList<>();

}