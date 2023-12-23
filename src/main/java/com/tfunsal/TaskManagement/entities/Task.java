package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.CommentDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    private TaskTag tag;

    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User assignee;

    public TaskDto getDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setCreatedDate(createdDate);
        taskDto.setDueDate(dueDate);
        taskDto.setStatus(status);
        taskDto.setTag(tag);
        if (assignee != null) {
            taskDto.setUserId(assignee.getId());
        }
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            commentDto.setContent(comment.getContent());
            commentDto.setCreatedDate(comment.getCreatedDate());
            commentDto.setUserId(comment.getAuthor().getId());
            commentDto.setTaskId(comment.getTask().getId());
            commentDtoList.add(commentDto);
        }
        taskDto.setComments(commentDtoList);
        return taskDto;
    }
}
