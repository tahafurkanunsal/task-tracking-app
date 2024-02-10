package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComments();

    List<CommentDto> getCommentsByTask(Long taskId);

    List<CommentDto> getCommentsByUser(Long userId);

    List<CommentDto> getCommentsByTaskIdAndProjectId(Long projectId, Long taskId);

    List<CommentDto> getUserCommentsByTaskIdAndProjectId(Long projectId, Long taskId, Long userId);

    TaskDto addCommentToTaskByUser(Long userId, Long projectId, Long taskId, CommentDto commentDto);

    TaskDto addCommentToTaskByAdmin(Long userId, Long projectId, Long taskId, CommentDto commentDto);

    CommentDto updateComment(Long projectId, Long taskId, Long commentId, CommentDto commentDto);

    boolean deleteComment(Long projectId, Long taskId, Long commentId);

}
