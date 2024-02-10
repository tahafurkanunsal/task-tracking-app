package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.services.AdminService;
import com.tfunsal.TaskManagement.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final CommentService commentService;


    @GetMapping("/projects")
    public ResponseEntity<List<ProjectInfoDto>> getAllProjects() {
        List<ProjectInfoDto> projectDtoList = adminService.getAllProjects();
        return ResponseEntity.ok(projectDtoList);
    }

    @GetMapping(value = "/projects", params = {"projectId"})
    public ResponseEntity<ProjectInfoDto> getProjectByProjectId(@RequestParam Long projectId) {
        ProjectInfoDto projectInfoDto = adminService.getProjectByProjectId(projectId);
        return ResponseEntity.ok(projectInfoDto);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasksDtoList = adminService.getAllTasks();
        return ResponseEntity.ok(tasksDtoList);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasksByProject(@PathVariable Long projectId) {
        List<TaskDto> taskDtoList = adminService.getAllTasksByProject(projectId);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping("/projects/{projectId}/users/{userId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasksByProjectIdAndUserId(@PathVariable Long projectId,
                                                                         @PathVariable Long userId) {
        try {
            List<TaskDto> tasks = adminService.getTasksByProjectForAUser(projectId, userId);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> commentDtoList = commentService.getAllComments();
        return ResponseEntity.ok(commentDtoList);
    }

    @GetMapping(value = "/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskIdAndProjectId(@PathVariable Long projectId,
                                                                            @PathVariable Long taskId) {

        try {
            List<CommentDto> commentDtoList = commentService.getCommentsByTaskIdAndProjectId(projectId, taskId);
            return ResponseEntity.ok(commentDtoList);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/projects/{projectId}/tasks/{taskId}/users/{userId}/comments")
    public ResponseEntity<List<CommentDto>> getUserCommentsByProjectIdAndTaskId(@PathVariable Long projectId,
                                                                                @PathVariable Long taskId,
                                                                                @PathVariable Long userId) {
        try {
            List<CommentDto> commentDtoList = commentService.getUserCommentsByTaskIdAndProjectId(projectId, taskId, userId);
            return ResponseEntity.ok(commentDtoList);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/tasks/comments", params = {"userId"})
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@RequestParam Long userId) {
        List<CommentDto> commentDtoList = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(commentDtoList);
    }

    @GetMapping(value = "/tasks", params = {"taskId"})
    public ResponseEntity<TaskDto> getTaskByTaskId(@RequestParam Long taskId) {
        TaskDto taskDto = adminService.getTaskByTaskId(taskId);
        return ResponseEntity.ok(taskDto);
    }

    @GetMapping(value = "/tasks", params = {"userId"})
    public ResponseEntity<List<TaskDto>> getTasksByUserId(@RequestParam Long userId) {
        List<TaskDto> taskDtoList = adminService.getTasksByUserId(userId);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"tag"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskTag(@RequestParam TaskTag tag) {
        List<TaskDto> taskDtoList = adminService.getTasksByTaskTag(tag);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"status"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskStatus(@RequestParam TaskStatus status) {
        List<TaskDto> taskDtoList = adminService.getTasksByTaskStatus(status);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"startDate", "endDate"})
    public ResponseEntity<List<TaskDto>> getTasksByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<TaskDto> taskDtoList = adminService.getTasksByDate(startDate, endDate);
        return ResponseEntity.ok(taskDtoList);
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectInfoDto> createProject(@RequestBody ProjectInfoDto projectInfoDto) {
        ProjectInfoDto newProjectDto = adminService.createProject(projectInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectDto);
    }

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long projectId, @RequestBody TaskCreateDto taskCreateDto) {
        TaskDto newTaskDto = adminService.assignATaskForProject(projectId, taskCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDto);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectInfoDto> updateProject(@PathVariable Long projectId, @RequestBody ProjectInfoDto projectInfoDto) {
        try {
            ProjectInfoDto updateProject = adminService.updateProject(projectId, projectInfoDto);
            return ResponseEntity.ok(updateProject);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody TaskDto taskDto) {

        try {
            TaskDto updatedTask = adminService.updateTask(projectId, taskId, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/assignee/{userId}")
    public ResponseEntity<TaskDto> assignAUserForTask(@PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @PathVariable Long userId) {
        try {
            TaskDto assignedTask = adminService.assignAUserForTask(projectId, taskId, userId);
            return ResponseEntity.ok(assignedTask);
        } catch (NoSuchTaskExistsException | TaskAlreadyAssignedException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/unassignee/{userId}")
    public ResponseEntity<TaskDto> unAssignAUserForTask(@PathVariable Long projectId,
                                                        @PathVariable Long taskId,
                                                        @PathVariable Long userId) {

        try {
            TaskDto unAssignedUserForTask = adminService.unAssignAUserForTask(projectId, taskId, userId);
            return ResponseEntity.ok(unAssignedUserForTask);
        } catch (NoSuchTaskExistsException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<TaskDto> addCommentToTaskByAdmin(Authentication authentication,
                                                           @PathVariable Long projectId,
                                                           @PathVariable Long taskId,
                                                           @RequestBody CommentDto commentDto) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        TaskDto taskDto = adminService.getTaskByTaskId(taskId);


        if (taskDto != null) {
            TaskDto addCommentTaskDto = commentService.addCommentToTaskByAdmin(userId, projectId, taskId, commentDto);

            return ResponseEntity.ok(addCommentTaskDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long projectId,
                                                    @PathVariable Long taskId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CommentDto commentDto) {

        try {
            CommentDto updatedComment = commentService.updateComment(projectId, taskId, commentId, commentDto);
            return ResponseEntity.ok(updatedComment);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException | CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        boolean deleted = adminService.deleteProject(projectId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId) {

        boolean deleted = adminService.deleteTask(projectId, taskId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @PathVariable Long commentId) {

        boolean deletedComment = commentService.deleteComment(projectId, taskId, commentId);

        if (deletedComment) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}