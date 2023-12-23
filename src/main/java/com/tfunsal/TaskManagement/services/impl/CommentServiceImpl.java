package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.Comment;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.exception.NoSuchTaskExistsException;
import com.tfunsal.TaskManagement.repository.CommentRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;


    public List<CommentDto> getAllComments(){
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(Comment::getDto).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByTask(Long taskId) {

        List<Comment> comments = commentRepository.findCommentsByTaskId(taskId);
        return comments.stream().map(Comment::getDto).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByUser(Long userId) {
        List<Comment> comments = commentRepository.findCommentsByAuthorId(userId);
        return comments.stream().map(Comment::getDto).collect(Collectors.toList());
    }

    public TaskDto addCommentToTaskByUser(Long userId, Long taskId, CommentDto commentDto) {


        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            if (existingTask.getAssignee() != null && existingTask.getAssignee().getId().equals(userId)) {

                Comment comment = new Comment();

                comment.setAuthor(userRepository.findById(userId).get());
                comment.setContent(commentDto.getContent());
                comment.setCreatedDate(LocalDateTime.now());
                comment.setTask(existingTask);

                existingTask.getComments().add(comment);

                taskRepository.save(existingTask);

                return existingTask.getDto();
            }
        } else {
            throw new NoSuchTaskExistsException("Task not found with id : " + taskId);
        }
        return null;
    }

    public TaskDto addCommentToTaskByAdmin(Long userId , Long taskId, CommentDto commentDto) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            Comment comment = new Comment();

            comment.setAuthor(userRepository.findById(userId).get());
            comment.setContent(commentDto.getContent());
            comment.setCreatedDate(LocalDateTime.now());
            comment.setTask(existingTask);

            existingTask.getComments().add(comment);

            taskRepository.save(existingTask);

            return existingTask.getDto();

        } else {
            throw new NoSuchTaskExistsException("Task not found with id : " + taskId);
        }
    }


    public CommentDto updateComment(Long taskId , Long commentId, CommentDto commentDto) {

        Comment existingComment = commentRepository.findById(commentId).get();

        existingComment.setContent(commentDto.getContent());
        existingComment.setCreatedDate(LocalDateTime.now());

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            existingComment.setTask(task);

        } else {
            throw new NoSuchTaskExistsException("Task not found with id : " + commentDto.getTaskId());
        }
        return commentRepository.save(existingComment).getDto();
    }

    public boolean delete(Long commentId) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
