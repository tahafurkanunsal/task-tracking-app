package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task , Long> {
}
