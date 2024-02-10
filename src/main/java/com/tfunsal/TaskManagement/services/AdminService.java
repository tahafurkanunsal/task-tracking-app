package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {

    List<ProjectInfoDto> getAllProjects();

    ProjectInfoDto getProjectByProjectId(Long projectId);

    List<TaskDto> getTasksByProjectForAUser(Long projectId, Long userId);

    ProjectInfoDto createProject(ProjectInfoDto projectInfoDto);

    ProjectInfoDto updateProject(Long projectId, ProjectInfoDto projectInfoDto);

    List<TaskDto> getAllTasks();

    List<TaskDto> getAllTasksByProject(Long projectId);

    TaskDto getTaskByTaskId(Long taskId);

    List<TaskDto> getTasksByUserId(Long userId);

    List<TaskDto> getTasksByTaskTag(TaskTag tag);

    List<TaskDto> getTasksByTaskStatus(TaskStatus taskStatus);

    List<TaskDto> getTasksByDate(LocalDateTime startDate, LocalDateTime endDate);

    TaskDto assignATaskForProject(Long ProjectId, TaskCreateDto taskCreateDto);

    TaskDto updateTask(Long projectId, Long taskId, TaskDto taskDto);

    TaskDto assignAUserForTask(Long projectId, Long taskId, Long userId);

    TaskDto unAssignAUserForTask(Long projectId, Long taskId, Long userId);

    boolean deleteProject(Long projectId);

    boolean deleteTask(Long projectId, Long taskId);


}
