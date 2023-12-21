package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

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
        return taskDto;
    }
}
