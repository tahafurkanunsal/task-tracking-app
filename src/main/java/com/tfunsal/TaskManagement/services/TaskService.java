package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.TaskCreateDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.dto.TaskUpdateDto;
import com.tfunsal.TaskManagement.dto.UserInfoDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    List<TaskDto> getAllTasksByProject(Long projectId, Long userId);

    TaskDto getTaskDetails(Long projectId, Long taskId, Long userId);

    List<TaskDto> getTasksByUserId(Long userId);

    List<TaskDto> getTasksByProjectForAUser(Long projectId, Long userId);

    List<TaskDto> getTasksByTaskTag(TaskTag tag);

    List<TaskDto> getTasksByTaskStatus(TaskStatus taskStatus);

    List<TaskDto> getTasksByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<UserInfoDto> getUsersAssignedToATask(Long projectId, Long taskId);

    TaskDto createTask(Long userId, Long projectId, TaskCreateDto taskCreateDto);

    TaskDto assignAUserForTask(Long projectId, Long taskId, Long userId);

    TaskDto unAssignAUserForTask(Long projectId, Long taskId, Long userId);

    TaskDto updateTask(Long projectId, Long taskId, Long userId, TaskUpdateDto taskUpdateDto);

    TaskDto changeTaskStatus(Long taskId, Long userId, TaskStatus status);

    boolean deleteTask(Long projectId, Long taskId, Long userId);

}
