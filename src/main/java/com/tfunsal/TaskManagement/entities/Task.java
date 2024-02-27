package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime dueDate;

    private LocalDateTime modifiedDate;

    private TaskTag tag;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public TaskDto getDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setCreatedDate(createdDate);
        taskDto.setModifiedDate(modifiedDate);
        taskDto.setDueDate(dueDate);
        taskDto.setStatus(status);
        taskDto.setTag(tag);
        taskDto.setProjectId(project.getId());
        taskDto.setProjectName(project.getName());

        List<Long> userIds = new ArrayList<>();
        if (assignees != null) {
            for (User assignee : assignees) {
                userIds.add(assignee.getId());
            }
        }
        taskDto.setUserIds(userIds != null ? userIds : new ArrayList<>());
        taskDto.setUserIds(userIds);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            commentDto.setContent(comment.getContent());
            commentDto.setCreatedDate(comment.getCreatedDate());
            commentDto.setModifiedDate(comment.getModifiedDate());
            commentDto.setUser(comment.getAuthor().getName());
            commentDto.setTaskId(comment.getTask().getId());
            commentDtoList.add(commentDto);
        }
        taskDto.setComments(commentDtoList);

        return taskDto;
    }
}