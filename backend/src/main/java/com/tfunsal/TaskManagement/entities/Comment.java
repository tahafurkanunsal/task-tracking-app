package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;


    public CommentDto getDto() {

        CommentDto commentDto = new CommentDto();

        commentDto.setId(id);
        commentDto.setContent(content);
        commentDto.setCreatedDate(createdDate);
        commentDto.setUser(author.getName());
        commentDto.setTaskId(task.getId());
        commentDto.setModifiedDate(modifiedDate);
        return commentDto;
    }
}