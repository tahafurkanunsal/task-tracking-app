package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.exception.NoSuchTaskExistsException;
import com.tfunsal.TaskManagement.exception.UnauthorizedTaskAccessException;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final TaskRepository taskRepository;

    public List<TaskDto> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByAssigneeId(userId);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    public TaskDto getTaskByUserIdAndTaskId(Long userId, Long taskId) {
        Task task = taskRepository.findByAssigneeIdAndId(userId, taskId);
        return task.getDto();
    }

    public TaskDto changeTaskStatus(Long taskId, TaskStatus status, Long userId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();

            if (existingTask.getAssignee() != null && existingTask.getAssignee().getId().equals(userId)) {
                existingTask.setStatus(status);

                Task updatedTask = taskRepository.save(existingTask);

                TaskDto taskDto = new TaskDto();
                taskDto.setId(updatedTask.getId());
                taskDto.setTitle(updatedTask.getTitle());
                taskDto.setDescription(updatedTask.getDescription());
                taskDto.setStatus(updatedTask.getStatus());
                taskDto.setTag(updatedTask.getTag());
                taskDto.setCreatedDate(LocalDateTime.now());
                taskDto.setDueDate(updatedTask.getDueDate());
                taskDto.setUserId(updatedTask.getAssignee().getId());

                return taskDto;
            } else {
                throw new UnauthorizedTaskAccessException("User does not have permission to update this task.");
            }
        } else {
            throw new NoSuchTaskExistsException("Task with ID " + taskId + " not found.");
        }
    }
}
