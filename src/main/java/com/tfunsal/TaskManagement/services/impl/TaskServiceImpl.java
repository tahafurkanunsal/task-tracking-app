package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.Company;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.CompanyRepository;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;


    @Override
    public List<TaskDto> getAllTasksByProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));


        if (project.getCompany().getCompanyOwner().getId().equals(userId)) {
            List<Task> tasks = taskRepository.findTasksByProjectId(projectId);
            return tasks.stream().map(Task::getDto).collect(Collectors.toList());
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public TaskDto getTaskDetails(Long projectId, Long taskId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id : " + taskId));

        if (task.getProject().getCompany().getCompanyOwner().getId().equals(userId)) {
            Task existingTask = taskRepository.findTaskByProjectIdAndId(projectId, taskId);
            return existingTask.getDto();
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public List<TaskDto> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByAssigneesId(userId);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByProjectForAUser(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id :" + projectId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (project.getCompany().getUsers().contains(user)) {
            List<Task> tasksInProject = project.getTasks();

            List<Task> tasksForUserInProject = tasksInProject.stream()
                    .filter(task -> task.getAssignees().contains(userId))
                    .collect(Collectors.toList());

            return tasksForUserInProject.stream()
                    .map(Task::getDto).collect(Collectors.toList());
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access the tasks of this company's this project.");
    }

    @Override
    public List<TaskDto> getTasksByTaskTag(TaskTag tag) {
        List<Task> tasks = taskRepository.findTasksByTag(tag);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByTaskStatus(TaskStatus taskStatus) {
        List<Task> tasks = taskRepository.findTasksByStatus(taskStatus);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Task> tasks = taskRepository.findTasksAtBetween(startDate, endDate);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public List<UserInfoDto> getUsersAssignedToATask(Long projectId, Long taskId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id :" + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));

        if (task.getProject().getId().equals(project.getId())) {
            List<UserInfoDto> assignedUsers = new ArrayList<>();
            List<User> userList = task.getAssignees();

            for (User assignee : userList) {
                UserInfoDto userInfo = new UserInfoDto();
                userInfo.setFullName(assignee.getName() + assignee.getSurname());
                userInfo.setEmail(assignee.getEmail());
                assignedUsers.add(userInfo);
            }
            return assignedUsers;
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access the tasks of this projects.");
    }

    @Override
    public TaskDto createTask(Long userId, Long projectId, TaskCreateDto taskCreateDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        if (project.getCompany().getCompanyOwner().getId().equals(userId)) {
            Task task = new Task();
            task.setTitle(taskCreateDto.getTitle());
            task.setDescription(taskCreateDto.getDescription());
            task.setCreatedDate(LocalDateTime.now());
            task.setModifiedDate(LocalDateTime.now());
            task.setDueDate(taskCreateDto.getDueDate());
            task.setStatus(taskCreateDto.getStatus());
            task.setTag(taskCreateDto.getTag());
            task.setProject(project);

            Task createdTask = taskRepository.save(task);
            return createdTask.getDto();
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");

    }

    @Override
    public TaskDto assignAUserForTask(Long projectId, Long taskId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id:" + userId));


        if (task.getProject().getId().equals(project.getId())) {
            if (task.getAssignees().contains(user)) {
                throw new TaskAlreadyAssignedException("User is already assigned to the task.");
            }
            task.getAssignees().add(user);
            Task assignedTask = taskRepository.save(task);
            return assignedTask.getDto();
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }


    @Override
    public TaskDto unAssignAUserForTask(Long projectId, Long taskId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id:" + userId));

        if (task.getProject().getId().equals(project.getId())) {
            List<User> assignees = task.getAssignees();
            if (task.getAssignees() == null || assignees.isEmpty()) {
                throw new TaskAlreadyUnassignedException("The task is not assigned to a user anyway.");
            }
            task.getAssignees().remove(user);

            Task unAssignedTask = taskRepository.save(task);
            return unAssignedTask.getDto();
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public TaskDto updateTask(Long projectId, Long taskId, Long userId, TaskUpdateDto taskUpdateDto) {

        Project project = projectRepository
                .findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));

        if (project.getCompany().getCompanyOwner().getId().equals(userId)) {
            if (task.getProject().getId().equals(project.getId())) {

                task.setTitle(taskUpdateDto.getTitle());
                task.setDescription(taskUpdateDto.getDescription());
                task.setStatus(taskUpdateDto.getStatus());
                task.setTag(taskUpdateDto.getTag());
                task.setModifiedDate(LocalDateTime.now());
                task.setDueDate(taskUpdateDto.getDueDate());
                task.setProject(project);

                if (taskUpdateDto.getUserIds() != null && !taskUpdateDto.getUserIds().isEmpty()) {
                    List<User> assignees = new ArrayList<>();
                    for (Long assignId : taskUpdateDto.getUserIds()) {
                        User assignee = userRepository.findById(assignId)
                                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
                        assignees.add(assignee);
                    }
                    task.setAssignees(assignees);
                } else {
                    task.setAssignees(null);
                }
                Task updatedTask = taskRepository.save(task);
                return updatedTask.getDto();
            }
            throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
        }
        throw new UnauthorizedCompanyAccessException("You are not authorized to access the tasks of this company's projects.");
    }

    @Override
    public TaskDto changeTaskStatus(Long taskId, Long userId, TaskStatus status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));


        List<User> assignees = task.getAssignees();
        if (assignees == null || assignees.isEmpty() || !assignees.stream().anyMatch(u -> u.getId().equals(userId))) {
            throw new UnauthorizedTaskAccessException("User does not have permission to update this task.");
        }

        task.setStatus(status);

        Task updatedTask = taskRepository.save(task);

        return updatedTask.getDto();
    }

    @Override
    public boolean deleteTask(Long projectId, Long taskId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id: " + taskId));

        if (project.getCompany().getCompanyOwner().getId().equals(userId)) {
            if (existingTask.getProject().getId().equals(project.getId())) {
                taskRepository.deleteById(taskId);
                return true;
            }
            return false;
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }
}
