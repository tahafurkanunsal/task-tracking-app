package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.CompanyDto;
import com.tfunsal.TaskManagement.dto.CompanyInfoDto;
import com.tfunsal.TaskManagement.dto.ProjectInfoDto;
import com.tfunsal.TaskManagement.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String companyName;

    private Boolean isActivate = false;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @OneToOne
    @JoinColumn(name = "company_owner_id", referencedColumnName = "id")
    private User companyOwner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;


    public CompanyDto getCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(id);
        companyDto.setCompanyName(companyName);
        companyDto.setCreatedDate(createdDate);
        companyDto.setModifiedDate(modifiedDate);
        companyDto.setActivate(isActivate);
        companyDto.setCompanyOwnerId(companyOwner.getId());
        companyDto.setCompanyOwnerName(companyOwner.getName());

        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFullName(user.getName() + " " + user.getSurname());
            userDto.setRole(user.getRole());
            userDto.setEmail(user.getEmail());

            userDto.setCompanyRoles(user.getCompanyRole());
            userDto.setCompanyId(user.getCompany().getId());
            userDto.setCompanyName(user.getCompany().getCompanyName());

            userDtoList.add(userDto);
        }
        companyDto.setUserDtos(userDtoList);

        List<ProjectInfoDto> projectInfoDtoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectInfoDto projectInfoDto = new ProjectInfoDto();
            projectInfoDto.setId(project.getId());
            projectInfoDto.setName(project.getName());
            projectInfoDto.setDescription(project.getDescription());
            projectInfoDto.setCreatedDate(project.getCreatedDate());
            projectInfoDto.setModifiedDate(project.getModifiedDate());
            projectInfoDto.setCompanyId(project.getCompany().getId());
            projectInfoDto.setCompanyName(project.getCompany().getCompanyName());

            projectInfoDtoList.add(projectInfoDto);
        }
        companyDto.setProjectInfoDtos(projectInfoDtoList);

        return companyDto;
    }

    public CompanyInfoDto getCompanyInfoDto() {
        CompanyInfoDto companyInfoDto = new CompanyInfoDto();
        companyInfoDto.setId(id);
        companyInfoDto.setCompanyName(companyName);
        companyInfoDto.setActivate(isActivate);
        companyInfoDto.setCreatedDate(createdDate);
        companyInfoDto.setModifiedDate(modifiedDate);
        companyInfoDto.setCompanyOwnerName(companyOwner.getName());
        companyInfoDto.setCompanyOwnerId(companyOwner.getId());

        return companyInfoDto;
    }
}