package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.ProjectCreateDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.ProjectUpdateDto;

import java.util.List;

public interface ProjectService {


    List<ProjectInfoDto> getAllProjectsByCompany(Long companyId, Long userId);

    ProjectInfoDto getProjectDetailsByCompany(Long companyId, Long projectId, Long userId);

    ProjectInfoDto createProjectForCompany(Long companyId, Long userId, ProjectCreateDto projectCreateDto);

    ProjectInfoDto updateProject(Long companyId, Long projectId, Long userId, ProjectUpdateDto projectUpdateDto);

    boolean deleteProject(Long companyId, Long projectId, Long userId);

}
