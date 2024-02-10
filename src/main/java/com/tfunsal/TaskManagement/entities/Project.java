package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.ProjectDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.TaskDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;


    public ProjectDto getDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(id);
        projectDto.setName(name);
        projectDto.setDescription(description);
        projectDto.setCreatedDate(createdDate);
        projectDto.setModifiedDate(modifiedDate);

        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : tasks) {
            TaskDto taskDto = new TaskDto();
            taskDto.setId(task.getId());
            taskDto.setTitle(task.getTitle());
            taskDto.setDescription(task.getDescription());
            taskDto.setStatus(task.getStatus());
            taskDto.setTag(task.getTag());
            taskDto.setCreatedDate(task.getCreatedDate());
            taskDto.setModifiedDate(task.getModifiedDate());
            taskDto.setDueDate(task.getDueDate());

            List<Long> assigneeIds = new ArrayList<>();
            for (User assignee : task.getAssignees()) {
                assigneeIds.add(assignee.getId());
            }
            taskDto.setUserIds(assigneeIds);

            taskDto.setProjectId(task.getProject().getId());
            taskDto.setProjectName(task.getProject().getName());
            taskDto.setComments(task.getDto().getComments());
            taskDtoList.add(taskDto);
        }
        projectDto.setTaskDtoList(taskDtoList);
        return projectDto;
    }


    public ProjectInfoDto getProjectInfoDto() {
        ProjectInfoDto projectInfoDto = new ProjectInfoDto();
        projectInfoDto.setId(id);
        projectInfoDto.setName(name);
        projectInfoDto.setDescription(description);
        projectInfoDto.setCreatedDate(createdDate);
        projectInfoDto.setModifiedDate(modifiedDate);
        return projectInfoDto;
    }
}
