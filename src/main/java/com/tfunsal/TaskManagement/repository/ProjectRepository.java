package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project ,Long > {
}
