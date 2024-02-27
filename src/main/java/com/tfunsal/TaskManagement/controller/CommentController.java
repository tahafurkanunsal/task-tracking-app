package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            List<CommentDto> commentDtoList = commentService.getAllComments(user.getId());
            return ResponseEntity.ok(commentDtoList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(value = "/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskIdAndProjectId(@PathVariable Long projectId,
                                                                            @PathVariable Long taskId,
                                                                            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<CommentDto> commentDtoList = commentService.getCommentsByTaskIdAndProjectId(projectId, taskId, user.getId());
            return ResponseEntity.ok(commentDtoList);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<TaskDto> addCommentToTask(Authentication authentication,
                                                    @PathVariable Long projectId,
                                                    @PathVariable Long taskId,
                                                    @RequestBody CommentDto commentDto) {

        try {
            User user = (User) authentication.getPrincipal();
            TaskDto taskDto = commentService.addCommentToTask(user.getId(), projectId, taskId, commentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedTaskAccessException | UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long projectId,
                                                    @PathVariable Long taskId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CommentDto commentDto,
                                                    Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            CommentDto updatedComment = commentService.updateComment(projectId, taskId, user.getId(), commentId, commentDto);
            return ResponseEntity.ok(updatedComment);
        } catch (ProjectNotFoundException | NoSuchTaskExistsException | CommentNotFoundException |
                 UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCommentAccessException | UnauthorizedTaskAccessException |
                 UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @PathVariable Long commentId,
                                              Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            boolean deletedComment = commentService.deleteComment(projectId, taskId, user.getId(), commentId);
            if (deletedComment) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ProjectNotFoundException | NoSuchTaskExistsException | CommentNotFoundException |
                 UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCommentAccessException | UnauthorizedTaskAccessException |
                 UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
