package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.Comment;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.CommentRepository;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;


    public List<CommentDto> getAllComments() {
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

    @Override
    public List<CommentDto> getCommentsByTaskIdAndProjectId(Long projectId, Long taskId) {
        List<CommentDto> commentsInProjectAndTask = new ArrayList<>();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        if (!task.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Task does not belong to the specified project.");
        }

        List<CommentDto> allCommentsForTask = getCommentsByTask(taskId);

        for (CommentDto comment : allCommentsForTask) {
            if (comment.getTaskId().equals(taskId)) {
                commentsInProjectAndTask.add(comment);
            }
        }

        return commentsInProjectAndTask;
    }

    @Override
    public List<CommentDto> getUserCommentsByTaskIdAndProjectId(Long projectId, Long taskId, Long userId) {
        List<CommentDto> userCommentsInProjectAndTask = new ArrayList<>();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("The task is not associated with the specified project.");
        }

        List<User> assignees = task.getAssignees();
        if (assignees == null || assignees.isEmpty() || !assignees.stream().anyMatch(u -> u.getId().equals(userId))) {
            throw new IllegalArgumentException("The task is not assigned to the specified user.");
        }

        List<CommentDto> allCommentsForTask = getCommentsByTask(taskId);

        for (CommentDto comment : allCommentsForTask) {
            if (comment.getUser().equals(user.getName())) {
                userCommentsInProjectAndTask.add(comment);
            }
        }

        return userCommentsInProjectAndTask;
    }

    public TaskDto addCommentToTaskByUser(Long userId, Long projectId, Long taskId, CommentDto commentDto) {

        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {

            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();

                List<User> assignees = task.getAssignees();

                if (assignees != null && !assignees.isEmpty() && assignees.stream().anyMatch(user -> user.getId().equals(userId))) {


                    Comment comment = new Comment();

                    comment.setAuthor(userRepository.findById(userId).get());
                    comment.setContent(commentDto.getContent());
                    comment.setCreatedDate(LocalDateTime.now());
                    comment.setModifiedDate(LocalDateTime.now());
                    comment.setTask(task);

                    task.getComments().add(comment);

                    taskRepository.save(task);

                    return task.getDto();
                } else {
                    throw new UnauthorizedTaskAccessException("User is not authorized to add comment to this task.");
                }
            } else {
                throw new NoSuchTaskExistsException("Task not found with id : " + taskId);
            }
        }
        throw new ProjectNotFoundException("Project not found with id:" + projectId);

    }

    public TaskDto addCommentToTaskByAdmin(Long userId, Long projectId, Long taskId, CommentDto commentDto) {

        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {

            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                Task existingTask = optionalTask.get();

                Comment comment = new Comment();

                comment.setAuthor(userRepository.findById(userId).get());
                comment.setContent(commentDto.getContent());
                comment.setCreatedDate(LocalDateTime.now());
                comment.setModifiedDate(LocalDateTime.now());
                comment.setTask(existingTask);

                existingTask.getComments().add(comment);

                taskRepository.save(existingTask);

                return existingTask.getDto();

            } else {
                throw new NoSuchTaskExistsException("Task not found with id : " + taskId);
            }
        }
        throw new ProjectNotFoundException("Project not found with id: " + projectId);
    }


    public CommentDto updateComment(Long projectId, Long taskId, Long commentId, CommentDto commentDto) {
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {

            Optional<Task> optionalTask = taskRepository.findById(taskId);

            if (optionalTask.isPresent()) {

                Task task = optionalTask.get();
                Optional<Comment> optionalComment = commentRepository.findById(commentId);

                if (optionalComment.isPresent()) {
                    Comment existingComment = optionalComment.get();

                    existingComment.setContent(commentDto.getContent());
                    existingComment.setModifiedDate(LocalDateTime.now());
                    existingComment.setTask(task);

                    return commentRepository.save(existingComment).getDto();
                } else {
                    throw new CommentNotFoundException("Comment not found with id: " + commentId);
                }
            } else {
                throw new NoSuchTaskExistsException("Task not found with id: " + taskId);
            }
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }

    @Override
    public boolean deleteComment(Long projectId, Long taskId, Long commentId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                Optional<Comment> optionalComment = commentRepository.findById(commentId);
                if (optionalComment.isPresent()) {
                    commentRepository.deleteById(commentId);
                    return true;
                } else {
                    throw new CommentNotFoundException("Comment not found with id: " + commentId);
                }
            } else {
                throw new NoSuchTaskExistsException("Task not found with id: " + taskId);
            }
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }
}