package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.exception.NoSuchTaskExistsException;
import com.tfunsal.TaskManagement.services.CommentService;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CommentService commentService;


    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectInfoDto> getProjectByProjectId(Authentication authentication,
                                                                @PathVariable Long projectId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);

        if (projectInfoDto != null) {
            return ResponseEntity.ok(projectInfoDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(Authentication authentication,
                                                             @PathVariable Long projectId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);

        if (projectInfoDto != null) {

            List<TaskDto> taskDtoList = userService.getTasksByProjectId(projectId, userId);
            return ResponseEntity.ok(taskDtoList);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTaskByProjectIdAndTaskIdAndUserId(Authentication authentication,
                                                                        @PathVariable Long projectId,
                                                                        @PathVariable Long taskId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);

        if (projectInfoDto != null) {

            TaskDto taskDto = userService.getTaskByProjectIdAndTaskIdAndUserId(projectId, taskId, userId);

            if (taskDto != null) {
                return ResponseEntity.ok(taskDto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        List<TaskDto> taskDtoList = userService.getTasksByUserId(userId);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskIdAndUserId(Authentication authentication,
                                                            @PathVariable Long taskId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        TaskDto taskDto = userService.getTaskByUserIdAndTaskId(userId, taskId);

        if (taskDto != null) {
            return ResponseEntity.ok(taskDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByTask(Authentication authentication,
                                                              @PathVariable Long projectId,
                                                              @PathVariable Long taskId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);
        if (projectInfoDto != null) {

            TaskDto taskDto = userService.getTaskByUserIdAndTaskId(userId, taskId);

            if (taskDto != null) {
                List<CommentDto> commentDtoList = commentService.getCommentsByTask(taskId);
                return ResponseEntity.ok(commentDtoList);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping("/projects/{projectId}/tasks/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(Authentication authentication, @PathVariable Long projectId) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);

        if (projectInfoDto != null) {

            List<TaskDto> taskDtoList = userService.getTasksByUserId(userId);

            if (taskDtoList != null) {
                List<CommentDto> commentDtoList = commentService.getCommentsByUser(userId);
                return ResponseEntity.ok(commentDtoList);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<TaskDto> addCommentToTaskByUser(Authentication authentication,
                                                          @PathVariable Long projectId,
                                                          @PathVariable Long taskId,
                                                          @RequestBody CommentDto commentDto) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);

        if (projectInfoDto != null) {


            TaskDto taskDto = userService.getTaskByUserIdAndTaskId(userId, taskId);

            if (taskDto != null) {
                TaskDto addCommentTaskDto = commentService.addCommentToTaskByUser(userId, projectId, taskId, commentDto);
                return ResponseEntity.ok(addCommentTaskDto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping(value = "/tasks/{taskId}/status", params = {"status"})
    public ResponseEntity<TaskDto> changeTaskStatus(Authentication authentication,
                                                    @PathVariable Long taskId,
                                                    @RequestParam TaskStatus status) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            TaskDto changeStatusTask = userService.changeTaskStatus(taskId, status, userId);
            return ResponseEntity.ok(changeStatusTask);
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(Authentication authentication,
                                                    @PathVariable Long projectId,
                                                    @PathVariable Long taskId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CommentDto commentDto) {

        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ProjectInfoDto projectInfoDto = userService.getProjectByProjectId(projectId, userId);
        if (projectInfoDto != null) {


            TaskDto taskDto = userService.getTaskByUserIdAndTaskId(userId, taskId);

            if (taskDto != null) {

                CommentDto updateComment = commentService.updateComment(projectId, taskId, commentId, commentDto);
                return ResponseEntity.ok(updateComment);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}