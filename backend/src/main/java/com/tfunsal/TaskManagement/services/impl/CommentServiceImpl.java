package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.entities.*;
import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.*;
import com.tfunsal.TaskManagement.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;


    @Override
    public List<CommentDto> getAllComments(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        Company company = companyRepository.findCompanyByCompanyOwnerId(user.getId());

        List<Project> projects = projectRepository.findProjectsByCompanyId(company.getId());

        List<CommentDto> allComments = new ArrayList<>();
        for (Project project : projects) {
            List<Task> tasks = taskRepository.findTasksByProjectId(project.getId());
            for (Task task : tasks) {
                List<Comment> comments = commentRepository.findCommentsByTaskId(task.getId());
                allComments.addAll(comments.stream().map(Comment::getDto).collect(Collectors.toList()));
            }
        }
        return allComments;
    }


    @Override
    public List<CommentDto> getCommentsByTaskIdAndProjectId(Long projectId, Long taskId, Long userId) {
        List<CommentDto> commentsInProjectAndTask = new ArrayList<>();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        if (task.getProject().getId().equals(project.getId())) {
            if (task.getAssignees().contains(user) || project.getCompany().getCompanyOwner().getId().equals(userId)) {

                List<Comment> allCommentsForTask = commentRepository.findCommentsByTaskId(taskId);

                for (Comment comment : allCommentsForTask) {
                    if (comment.getTask().getId().equals(taskId)) {
                        commentsInProjectAndTask.add(comment.getDto());
                    }
                }
                return commentsInProjectAndTask;
            }
            throw new UnauthorizedTaskAccessException("User does not have permission to update this task.");
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public TaskDto addCommentToTask(Long userId, Long projectId, Long taskId, CommentDto commentDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        if (task.getProject().getId().equals(project.getId())) {
            if (task.getAssignees().contains(user) || project.getCompany().getCompanyOwner().getId().equals(userId)) {

                Comment comment = new Comment();

                comment.setAuthor(userRepository.findById(userId).get());
                comment.setContent(commentDto.getContent());
                comment.setCreatedDate(LocalDateTime.now());
                comment.setModifiedDate(LocalDateTime.now());
                comment.setTask(task);

                task.getComments().add(comment);
                taskRepository.save(task);

                return task.getDto();
            }
            throw new UnauthorizedTaskAccessException("User is not authorized to add comment to this task.");
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public CommentDto updateComment(Long projectId, Long taskId, Long userId, Long commentId, CommentDto commentDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id : " + commentId));

        if (task.getProject().getId().equals(project.getId())) {
            if (task.getAssignees().contains(user) || project.getCompany().getCompanyOwner().getId().equals(userId)) {
                if (comment.getAuthor().getId().equals(userId) || (user.getRole() == UserRole.COMPANY_ADMIN && comment.getAuthor().getId().equals(userId))) {

                    comment.setContent(commentDto.getContent());
                    comment.setModifiedDate(LocalDateTime.now());

                    commentRepository.save(comment);

                    return comment.getDto();
                }
                throw new UnauthorizedCommentAccessException("You are not authorized to access this comment.");
            }
            throw new UnauthorizedTaskAccessException("User is not authorized to add comment to this task.");
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }

    @Override
    public boolean deleteComment(Long projectId, Long taskId, Long userId, Long commentId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskExistsException("Task not found with id :" + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id : " + commentId));

        if (task.getProject().getId().equals(project.getId())) {
            if (task.getAssignees().contains(user) || project.getCompany().getCompanyOwner().getId().equals(userId)) {
                if (comment.getAuthor().getId().equals(userId) || user.getRole() == UserRole.COMPANY_ADMIN && comment.getAuthor().getId().equals(userId)) {

                    commentRepository.deleteById(commentId);
                    return true;
                }
                throw new UnauthorizedCommentAccessException("You are not authorized to access this comment.");
            }
            throw new UnauthorizedTaskAccessException("User is not authorized to add comment to this task.");
        }
        throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
    }
}