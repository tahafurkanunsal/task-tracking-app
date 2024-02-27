package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.TaskCreateDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.dto.TaskUpdateDto;
import com.tfunsal.TaskManagement.dto.UserInfoDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasksByProject(@PathVariable Long projectId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<TaskDto> taskDtoList = taskService.getAllTasksByProject(projectId, user.getId());
            return ResponseEntity.ok(taskDtoList);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @GetMapping("/projects/{projectId}/tasks/{taskId}/details")
    public ResponseEntity<TaskDto> getTaskDetails(@PathVariable Long projectId,
                                                  @PathVariable Long taskId,
                                                  Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            TaskDto taskDto = taskService.getTaskDetails(projectId, taskId, user.getId());
            return ResponseEntity.ok(taskDto);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }


    @GetMapping("/all-tasks") // users
    public ResponseEntity<List<TaskDto>> getTasksByUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<TaskDto> taskDtoList = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(taskDtoList);
    }


    @GetMapping("/projects/{projectId}/users/{userId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByProjectForAUser(@PathVariable Long projectId,
                                                                   @PathVariable Long userId) {

        try {
            List<TaskDto> taskDtoList = taskService.getTasksByProjectForAUser(projectId, userId);
            return ResponseEntity.ok(taskDtoList);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @GetMapping(value = "/tasks", params = {"tag"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskTag(@RequestParam TaskTag tag) {
        List<TaskDto> taskDtoList = taskService.getTasksByTaskTag(tag);
        return ResponseEntity.ok(taskDtoList);
    }


    @GetMapping(value = "/tasks", params = {"status"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskStatus(@RequestParam TaskStatus status) {
        List<TaskDto> taskDtoList = taskService.getTasksByTaskStatus(status);
        return ResponseEntity.ok(taskDtoList);
    }


    @GetMapping(value = "/tasks", params = {"startDate", "endDate"})
    public ResponseEntity<List<TaskDto>> getTasksByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<TaskDto> taskDtoList = taskService.getTasksByDate(startDate, endDate);
        return ResponseEntity.ok(taskDtoList);
    }


    @GetMapping("/projects/{projectId}/tasks/{taskId}/assigned-users")
    public ResponseEntity<List<UserInfoDto>> getUsersAssignedToATask(@PathVariable Long projectId, @PathVariable Long taskId) {
        try {
            List<UserInfoDto> assignedUserList = taskService.getUsersAssignedToATask(projectId, taskId);
            return ResponseEntity.ok(assignedUserList);
        } catch (NoSuchTaskExistsException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long projectId,
                                              @RequestBody TaskCreateDto taskCreateDto,
                                              Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            TaskDto newTaskDto = taskService.createTask(user.getId(), projectId, taskCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDto);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}/assignee/{userId}")
    public ResponseEntity<TaskDto> assignAUserForTask(@PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @PathVariable Long userId) {
        try {
            TaskDto assignedTask = taskService.assignAUserForTask(projectId, taskId, userId);
            return ResponseEntity.ok(assignedTask);
        } catch (NoSuchTaskExistsException | UserNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (TaskAlreadyAssignedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}/unassignee/{userId}")
    public ResponseEntity<TaskDto> unAssignAUserForTask(@PathVariable Long projectId,
                                                        @PathVariable Long taskId,
                                                        @PathVariable Long userId) {

        try {
            TaskDto unAssignedUserForTask = taskService.unAssignAUserForTask(projectId, taskId, userId);
            return ResponseEntity.ok(unAssignedUserForTask);
        } catch (NoSuchTaskExistsException | UserNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (TaskAlreadyUnassignedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestBody TaskUpdateDto taskUpdateDto,
                                              Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            TaskDto updatedTask = taskService.updateTask(projectId, taskId, user.getId(), taskUpdateDto);
            return ResponseEntity.ok(updatedTask);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCompanyAccessException | UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping(value = "/tasks/{taskId}/status", params = {"status"})
    public ResponseEntity<TaskDto> changeTaskStatus(Authentication authentication,
                                                    @PathVariable Long taskId,
                                                    @RequestParam TaskStatus status) {
        try {
            User user = (User) authentication.getPrincipal();
            TaskDto changeStatusTask = taskService.changeTaskStatus(taskId, user.getId(), status);
            return ResponseEntity.ok(changeStatusTask);
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedTaskAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId,
                                           Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            boolean deleted = taskService.deleteTask(projectId, taskId, user.getId());

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ProjectNotFoundException | NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
