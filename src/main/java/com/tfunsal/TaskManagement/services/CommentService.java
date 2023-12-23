package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComments();
    List<CommentDto> getCommentsByTask(Long taskId);

    List<CommentDto> getCommentsByUser(Long userId);

    TaskDto addCommentToTaskByUser(Long userId , Long taskId, CommentDto commentDto);

    TaskDto addCommentToTaskByAdmin(Long userId , Long taskId, CommentDto commentDto);

    CommentDto updateComment(Long taskId ,Long commentId, CommentDto commentDto);

    boolean delete(Long commentId);
}
