package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComments(Long userId);

    List<CommentDto> getCommentsByTaskIdAndProjectId(Long projectId, Long taskId, Long userId);

    TaskDto addCommentToTask(Long userId, Long projectId, Long taskId, CommentDto commentDto);

    CommentDto updateComment(Long projectId, Long taskId, Long userId, Long commentId, CommentDto commentDto);

    boolean deleteComment(Long projectId, Long taskId, Long userId, Long commentId);

}
