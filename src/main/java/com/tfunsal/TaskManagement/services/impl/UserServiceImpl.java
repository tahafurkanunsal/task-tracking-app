package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.CompanyRole;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public UserUpdateDto updateUserRole(Long userId , UserRole role){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setRole(role);
        User updateUser = userRepository.save(user);

        return updateUser.getUserUpdateDto();

    }


//
//    @Override
//    public ProjectInfoDto getProjectByProjectId(Long projectId, Long userId) {
//        List<Task> taskList = taskRepository.findByAssigneesId(userId);
//
//        Optional<Task> projectTask = taskList.stream()
//                .filter(task -> task.getProject().getId().equals(projectId))
//                .findFirst();
//
//        if (projectTask.isPresent()) {
//            Project project = projectRepository.findById(projectId)
//                    .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
//            return project.getProjectInfoDto();
//        }
//        throw new UnauthorizedProjectAccessException("User with ID " + userId + " does not have permission to view project with ID " + projectId);
//    }
//
//    @Override
//    public List<TaskDto> getTasksByUserId(Long userId) {
//        List<Task> tasks = taskRepository.findByAssigneesId(userId);
//        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<TaskDto> getTasksByProjectId(Long projectId, Long userId) {
//
//        List<Task> tasks = taskRepository.findByProjectIdAndAssigneesId(projectId, userId);
//        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
//    }
//
//    @Override
//    public TaskDto getTaskByUserIdAndTaskId(Long userId, Long taskId) {
//        Task task = taskRepository.findByAssigneesIdAndId(userId, taskId);
//        return task.getDto();
//    }
//
//    @Override
//    public TaskDto getTaskByProjectIdAndTaskIdAndUserId(Long projectId, Long taskId, Long userId) {
//        Task task = taskRepository.findByProjectIdAndIdAndAssigneesId(projectId, taskId, userId);
//        return task.getDto();
//    }
//
//    @Override
//    public TaskDto changeTaskStatus(Long taskId, TaskStatus status, Long userId) {
//        Optional<Task> taskOptional = taskRepository.findById(taskId);
//
//        if (taskOptional.isPresent()) {
//            Task existingTask = taskOptional.get();
//
//            List<User> assignees = existingTask.getAssignees();
//            if (assignees == null || assignees.isEmpty() || !assignees.stream().anyMatch(u -> u.getId().equals(userId))) {
//                throw new UnauthorizedTaskAccessException("User does not have permission to update this task.");
//            }
//
//            existingTask.setStatus(status);
//
//            Task updatedTask = taskRepository.save(existingTask);
//
//            TaskDto taskDto = new TaskDto();
//            taskDto.setId(updatedTask.getId());
//            taskDto.setTitle(updatedTask.getTitle());
//            taskDto.setDescription(updatedTask.getDescription());
//            taskDto.setStatus(updatedTask.getStatus());
//            taskDto.setTag(updatedTask.getTag());
//            taskDto.setCreatedDate(updatedTask.getCreatedDate());
//            taskDto.setModifiedDate(LocalDateTime.now());
//            taskDto.setDueDate(updatedTask.getDueDate());
//            taskDto.setProjectName(existingTask.getProject().getName());
//            taskDto.setProjectId(existingTask.getProject().getId());
//
//            List<Long> userIds = new ArrayList<>();
//            for (User assignee : updatedTask.getAssignees()) {
//                userIds.add(assignee.getId());
//            }
//            taskDto.setUserIds(userIds);
//
//            return taskDto;
//        } else {
//            throw new NoSuchTaskExistsException("Task with ID " + taskId + " not found.");
//        }
//    }
}
