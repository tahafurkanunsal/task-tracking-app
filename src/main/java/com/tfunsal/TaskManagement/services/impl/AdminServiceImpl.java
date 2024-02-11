package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;


    @Override
    public List<ProjectInfoDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(Project::getProjectInfoDto).collect(Collectors.toList());
    }

    @Override
    public ProjectInfoDto getProjectByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found." + projectId));
        return project.getProjectInfoDto();
    }

    @Override
    public List<TaskDto> getTasksByProjectForAUser(Long projectId, Long userId) {
        ProjectInfoDto projectInfoDto = getProjectByProjectId(projectId);
        List<TaskDto> tasksInProjectForUser = new ArrayList<>();

        if (projectInfoDto != null) {
            List<TaskDto> allTasksForUser = getTasksByUserId(userId);
            boolean tasks = false;

            for (TaskDto task : allTasksForUser) {
                if (task.getProjectId().equals(projectId)) {
                    tasksInProjectForUser.add(task);
                    tasks = true;
                }
            }
            if (!tasks) {
                throw new IllegalArgumentException("The user does not have a task in the specified project.");
            }
        }

        return tasksInProjectForUser;
    }


    @Override
    public ProjectInfoDto createProject(ProjectInfoDto projectInfoDto) {
        Project project = new Project();

        project.setId(projectInfoDto.getId());
        project.setName(projectInfoDto.getName());
        project.setDescription(projectInfoDto.getDescription());
        project.setCreatedDate(LocalDateTime.now());
        project.setModifiedDate(LocalDateTime.now());

        return projectRepository.save(project).getProjectInfoDto();
    }

    @Override
    public ProjectInfoDto updateProject(Long projectId, ProjectInfoDto projectInfoDto) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Project existingProject = optionalProject.get();

            if (projectInfoDto.getName() != null) {
                existingProject.setName(projectInfoDto.getName());
            }
            if (projectInfoDto.getDescription() != null) {
                existingProject.setDescription(projectInfoDto.getDescription());
            }
            existingProject.setModifiedDate(LocalDateTime.now());

            Project savedProject = projectRepository.save(existingProject);
            return savedProject.getProjectInfoDto();
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllTasksByProject(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchTaskExistsException("Task not found." + taskId));
        return task.getDto();
    }

    @Override
    public List<TaskDto> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByAssigneesId(userId);
        return tasks.stream().map(Task::getDto).collect(Collectors.toList());
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
    public TaskDto assignATaskForProject(Long projectId, TaskCreateDto taskCreateDto) {

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            Task task = new Task();
            task.setTitle(taskCreateDto.getTitle());
            task.setDescription(taskCreateDto.getDescription());
            task.setCreatedDate(LocalDateTime.now());
            task.setModifiedDate(LocalDateTime.now());
            task.setDueDate(taskCreateDto.getDueDate());
            task.setStatus(taskCreateDto.getStatus());
            task.setTag(taskCreateDto.getTag());
            task.setProject(project);


            if (taskCreateDto.getUserIds() != null && !taskCreateDto.getUserIds().isEmpty()) {
                List<User> assignees = new ArrayList<>();
                for (Long userId : taskCreateDto.getUserIds()) {
                    User assignee = userRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
                    assignees.add(assignee);
                }
                task.setAssignees(assignees);
            } else {
                task.setAssignees(null);
            }
            Task savedTask = taskRepository.save(task);
            return savedTask.getDto();
        }
        throw new ProjectNotFoundException("Project not found with id:" + projectId);

    }

    @Override
    public TaskDto updateTask(Long projectId, Long taskId, TaskDto taskDto) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
            if (existingTaskOptional.isPresent()) {
                Task task = existingTaskOptional.get();

                if (!task.getProject().getId().equals(projectId)) {
                    throw new IllegalArgumentException("Task does not belong to the specified project.");
                }

                task.setTitle(taskDto.getTitle());
                task.setStatus(taskDto.getStatus());
                task.setTag(taskDto.getTag());
                task.setDescription(taskDto.getDescription());
                task.setModifiedDate(LocalDateTime.now());
                task.setDueDate(taskDto.getDueDate());

                if (taskDto.getProjectId() != null) {
                    Optional<Project> projectOptional = projectRepository.findById(taskDto.getProjectId());
                    Project editProject = projectOptional.orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + taskDto.getProjectId()));
                    task.setProject(editProject);
                } else {
                    task.setProject(project);
                }

                if (taskDto.getUserIds() != null && !taskDto.getUserIds().isEmpty()) {
                    List<User> assignees = new ArrayList<>();
                    for (Long userId : taskDto.getUserIds()) {
                        User assignee = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
                        assignees.add(assignee);
                    }
                    task.setAssignees(assignees);
                } else {
                    task.setAssignees(null);
                }

                Task updatedTask = taskRepository.save(task);
                return updatedTask.getDto();
            } else {
                throw new NoSuchTaskExistsException("Task not found with id: " + taskId);
            }
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }


    @Override
    public TaskDto assignAUserForTask(Long projectId, Long taskId, Long userId) {


        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id:" + taskId));

        if (!task.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Task does not belong to the specified project.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id:" + userId));


        if (task.getAssignees().contains(user)) {
            throw new TaskAlreadyAssignedException("User is already assigned to the task.");
        }

        task.getAssignees().add(user);

        Task updatedTask = taskRepository.save(task);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(updatedTask.getId());
        taskDto.setTitle(updatedTask.getTitle());
        taskDto.setDescription(updatedTask.getDescription());
        taskDto.setStatus(updatedTask.getStatus());
        taskDto.setTag(updatedTask.getTag());
        taskDto.setCreatedDate(updatedTask.getCreatedDate());
        taskDto.setModifiedDate(LocalDateTime.now());
        taskDto.setDueDate(updatedTask.getDueDate());
        taskDto.setProjectName(updatedTask.getProject().getName());
        taskDto.setProjectId(updatedTask.getProject().getId());

        Set<Long> userIds = new HashSet<>();

        for (User assignee : updatedTask.getAssignees()) {
            userIds.add(assignee.getId());
        }
        taskDto.setUserIds(new ArrayList<>(userIds));

        return taskDto;
    }

    @Override
    public TaskDto unAssignAUserForTask(Long projectId, Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id:" + taskId));

        if (!task.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Task does not belong to the specified project.");
        }

        List<User> assignees = task.getAssignees();
        if (task.getAssignees() == null || assignees.isEmpty()) {
            throw new TaskAlreadyUnassignedException("The task is not assigned to a user anyway.");
        }

        User userToRemove = null;
        for (User assignee : assignees) {
            if (assignee.getId().equals(userId)) {
                userToRemove = assignee;
                break;
            }
        }

        if (userToRemove == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }


        task.getAssignees().remove(userToRemove);
        Task updatedTask = taskRepository.save(task);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(updatedTask.getId());
        taskDto.setTitle(updatedTask.getTitle());
        taskDto.setDescription(updatedTask.getDescription());
        taskDto.setStatus(updatedTask.getStatus());
        taskDto.setTag(updatedTask.getTag());
        taskDto.setCreatedDate(updatedTask.getCreatedDate());
        taskDto.setModifiedDate(LocalDateTime.now());
        taskDto.setDueDate(updatedTask.getDueDate());
        taskDto.setProjectId(updatedTask.getProject().getId());
        taskDto.setProjectName(updatedTask.getProject().getName());

        List<Long> userIds = new ArrayList<>();
        for (User assign : updatedTask.getAssignees()) {
            userIds.add(assign.getId());
        }
        taskDto.setUserIds(userIds);

        return taskDto;
    }


    @Override
    public boolean deleteProject(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            projectRepository.deleteById(projectId);
            return true;
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }

    public boolean deleteTask(Long projectId, Long taskId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                taskRepository.deleteById(taskId);
                return true;
            } else {
                throw new NoSuchTaskExistsException("Task not found with id: " + taskId);
            }
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }
}