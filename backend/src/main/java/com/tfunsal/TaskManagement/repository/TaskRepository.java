package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByAssigneesId(Long userId);

    List<Task> findTasksByProjectId(Long projectId);

    Task findTaskByProjectIdAndId(Long projectId, Long taskId);

    List<Task> findTasksByTag(TaskTag tag);

    List<Task> findTasksByStatus(TaskStatus taskStatus);

    @Query("SELECT t  FROM Task t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
