package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findProjectsByCompanyId(Long companyId);

    Project findProjectByCompanyIdAndId(Long companyId, Long projectId);
}
