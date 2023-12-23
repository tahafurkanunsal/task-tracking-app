package com.tfunsal.TaskManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private LocalDateTime createdDate;

    private Long userId;

    private Long taskId;

}
