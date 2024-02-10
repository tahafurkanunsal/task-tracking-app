package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;

import java.util.List;

public interface UserService {


    ProjectInfoDto getProjectByProjectId(Long projectId, Long userId);

    List<TaskDto> getTasksByUserId(Long userId);

    List<TaskDto> getTasksByProjectId(Long projectId, Long userId);

    TaskDto getTaskByUserIdAndTaskId(Long userId, Long taskId);

    TaskDto getTaskByProjectIdAndTaskIdAndUserId(Long projectId, Long taskId, Long userId);

    TaskDto changeTaskStatus(Long taskId, TaskStatus status, Long userId);
}
