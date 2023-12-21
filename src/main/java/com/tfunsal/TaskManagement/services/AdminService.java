package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {

    List<TaskDto> getAllTasks();

    TaskDto getTaskByTaskId(Long taskId);

    List<TaskDto> getTasksByUserId(Long userId);

    List<TaskDto> getTasksByTaskTag(TaskTag tag);

    List<TaskDto> getTasksByTaskStatus(TaskStatus taskStatus);

    List<TaskDto> getTasksByDate(LocalDateTime startDate, LocalDateTime endDate);

    TaskDto create(TaskDto taskDto);

    TaskDto update(Long taskId, TaskDto taskDto);

    TaskDto assignAUserForTask(Long taskId, Long userId);

    boolean delete(Long taskId);

}
