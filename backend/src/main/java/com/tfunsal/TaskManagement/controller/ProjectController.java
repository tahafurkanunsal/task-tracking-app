package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.ProjectCreateDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.ProjectUpdateDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.exception.CompanyNotFoundException;
import com.tfunsal.TaskManagement.exception.ProjectNotFoundException;
import com.tfunsal.TaskManagement.exception.UnauthorizedCompanyAccessException;
import com.tfunsal.TaskManagement.exception.UnauthorizedProjectAccessException;
import com.tfunsal.TaskManagement.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/companies/{companyId}/all-projects")
    public ResponseEntity<List<ProjectInfoDto>> getAllProjectsByCompany(@PathVariable Long companyId,
                                                                        Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            List<ProjectInfoDto> projectInfoDtoList = projectService.getAllProjectsByCompany(companyId, user.getId());
            return ResponseEntity.ok(projectInfoDtoList);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCompanyAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("/companies/{companyId}/projects/{projectId}")
    public ResponseEntity<ProjectInfoDto> getProjectDetailsByCompany(@PathVariable Long companyId,
                                                                     @PathVariable Long projectId,
                                                                     Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            ProjectInfoDto projectInfoDto = projectService.getProjectDetailsByCompany(companyId, projectId, user.getId());
            return ResponseEntity.ok(projectInfoDto);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCompanyAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/companies/{companyId}/projects")
    public ResponseEntity<ProjectInfoDto> createProjectForCompany(@PathVariable Long companyId,
                                                                  @RequestBody ProjectCreateDto projectCreateDto,
                                                                  Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            ProjectInfoDto projectInfoDto = projectService.createProjectForCompany(companyId, user.getId(), projectCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectInfoDto);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCompanyAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PutMapping("/companies/{companyId}/projects/{projectId}")
    public ResponseEntity<ProjectInfoDto> updateProject(@PathVariable Long companyId,
                                                        @PathVariable Long projectId,
                                                        @RequestBody ProjectUpdateDto projectUpdateDto,
                                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            ProjectInfoDto updatedProject = projectService.updateProject(companyId, projectId, user.getId(), projectUpdateDto);
            return ResponseEntity.ok(updatedProject);
        } catch (CompanyNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedCompanyAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/companies/{companyId}/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long companyId,
                                              @PathVariable Long projectId,
                                              Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            boolean deleted = projectService.deleteProject(companyId, projectId, user.getId());

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (CompanyNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedCompanyAccessException | UnauthorizedProjectAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
