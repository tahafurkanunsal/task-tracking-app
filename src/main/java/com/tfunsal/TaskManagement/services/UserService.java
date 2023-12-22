package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;

import java.util.List;

public interface UserService {


    List<TaskDto> getTasksByUserId(Long userId);

    TaskDto getTaskByUserIdAndTaskId(Long userId, Long taskId);
    TaskDto changeTaskStatus(Long taskId , TaskStatus status, Long userId);



}
