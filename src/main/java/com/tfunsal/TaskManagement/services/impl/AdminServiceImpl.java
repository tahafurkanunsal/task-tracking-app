package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.NoSuchTaskExistsException;
import com.tfunsal.TaskManagement.exception.UserNotFoundException;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;


    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    public TaskDto getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchTaskExistsException("Task not found."));
        return task.getDto();
    }

    public List<TaskDto> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByAssigneeId(userId);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByTaskTag(TaskTag tag) {
        List<Task> tasks = taskRepository.findTasksByTag(tag);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByTaskStatus(TaskStatus taskStatus) {
        List<Task> tasks = taskRepository.findTasksByStatus(taskStatus);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Task> tasks = taskRepository.findTasksAtBetween(startDate, endDate);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());

    }

    public TaskDto create(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedDate(LocalDateTime.now());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(taskDto.getStatus());
        task.setTag(taskDto.getTag());

        if (taskDto.getUserId() != null) {
            User assignee = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + taskDto.getUserId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        return taskRepository.save(task).getDto();
    }

    public TaskDto update(Long taskId, TaskDto taskDto) {

        Task existingTask = taskRepository.findById(taskId).get();

        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setDueDate(taskDto.getDueDate());
        existingTask.setCreatedDate(LocalDateTime.now());
        existingTask.setStatus(taskDto.getStatus());
        existingTask.setTag(taskDto.getTag());

        Optional<User> optionalUser = userRepository.findById(taskDto.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            existingTask.setAssignee(user);

        } else {
            throw new UserNotFoundException("User not found with id : " + taskDto.getUserId());
        }
        return taskRepository.save(existingTask).getDto();
    }

    public TaskDto assignAUserForTask(Long taskId, Long userId) {

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (existingTask != null && existingTask.getAssignee() == null) {
            User user = userRepository.findById(userId).orElseThrow();
            existingTask.setAssignee(user);

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
        }
        return null;
    }

    public boolean delete(Long taskId) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }
}
