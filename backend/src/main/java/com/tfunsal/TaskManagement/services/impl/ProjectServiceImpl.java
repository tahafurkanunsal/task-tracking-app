package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.ProjectCreateDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.ProjectUpdateDto;
import com.tfunsal.TaskManagement.entities.Company;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.exception.CompanyNotFoundException;
import com.tfunsal.TaskManagement.exception.ProjectNotFoundException;
import com.tfunsal.TaskManagement.exception.UnauthorizedCompanyAccessException;
import com.tfunsal.TaskManagement.exception.UnauthorizedProjectAccessException;
import com.tfunsal.TaskManagement.repository.CompanyRepository;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<ProjectInfoDto> getAllProjectsByCompany(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id :" + companyId));

        if (company.getCompanyOwner().getId().equals(userId)) {
            List<Project> projects = projectRepository.findProjectsByCompanyId(companyId);
            return projects.stream().map(Project::getProjectInfoDto).collect(Collectors.toList());
        }
        throw new UnauthorizedCompanyAccessException("You are not authorized to access projects of this company.");
    }

    @Override
    public ProjectInfoDto getProjectDetailsByCompany(Long companyId, Long projectId, Long userId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id :" + companyId));


        Project project = projectRepository.findProjectByCompanyIdAndId(companyId, projectId);

        if (company.getCompanyOwner().getId().equals(userId)) {
            return project.getProjectInfoDto();
        }
        throw new UnauthorizedCompanyAccessException("You are not authorized to access projects of this company.");
    }

    @Override
    public ProjectInfoDto createProjectForCompany(Long companyId, Long userId, ProjectCreateDto projectCreateDto) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id : " + companyId));

        if (!company.getCompanyOwner().getId().equals(userId)) {
            throw new UnauthorizedCompanyAccessException("Only the company owner can create projects for this company.");
        }

        Project project = new Project();
        project.setName(projectCreateDto.getName());
        project.setCreatedDate(LocalDateTime.now());
        project.setModifiedDate(LocalDateTime.now());
        project.setCompany(company);
        project.setDescription(projectCreateDto.getDescription());

        return projectRepository.save(project).getProjectInfoDto();
    }

    @Override
    public ProjectInfoDto updateProject(Long companyId, Long projectId, Long userId, ProjectUpdateDto projectUpdateDto) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id : " + companyId));

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        if (existingProject.getCompany().getCompanyOwner().getId().equals(userId)) {
            if (!existingProject.getCompany().getId().equals(company.getId())) {
                throw new UnauthorizedCompanyAccessException("You are not authorized to update projects of this company.");
            }

            existingProject.setName(projectUpdateDto.getName());
            existingProject.setDescription(projectUpdateDto.getDescription());
            existingProject.setModifiedDate(LocalDateTime.now());

            return projectRepository.save(existingProject).getProjectInfoDto();
        } else {
            throw new UnauthorizedCompanyAccessException("Only the company owner can update projects for this company.");
        }
    }


    @Override
    public boolean deleteProject(Long companyId, Long projectId, Long userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id : " + companyId));

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id : " + projectId));

        if (existingProject.getCompany().getCompanyOwner().getId().equals(userId)) {
            if (existingProject.getCompany().getId().equals(company.getId())) {
                projectRepository.deleteById(projectId);
                return true;
            } else {
                throw new UnauthorizedProjectAccessException("You are not authorized to access this project.");
            }
        } else {
            throw new UnauthorizedCompanyAccessException("Only the company owner can delete projects for this company.");
        }
    }
}
