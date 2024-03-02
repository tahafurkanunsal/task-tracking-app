package com.tfunsal.TaskManagement.services.impl;


import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.Company;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.CompanyRole;
import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.CompanyRepository;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.CompanyService;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {


    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    @Override
    public List<CompanyInfoDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(Company::getCompanyInfoDto).collect(Collectors.toList());
    }


    @Override
    public CompanyDto getCompanyDetails(Long companyId, Long userId) {
        Company company = companyRepository.findCompanyByIdAndCompanyOwnerId(companyId, userId);
        return company.getCompanyDto();
    }


    @Override
    public CompanyDto getCompanyByCompanyName(String companyName) {
        Company company = companyRepository.findCompanyByCompanyName(companyName);
        return company.getCompanyDto();
    }


    @Override
    public List<CompanyInfoDto> getPendingCompanies() {
        List<Company> companies = companyRepository.findCompanyByIsActivate(false);
        return companies.stream().map(Company::getCompanyInfoDto).collect(Collectors.toList());
    }


    @Override
    public CompanyProjectDto getCompanyProjectsByCompanyId(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        if (!company.getCompanyOwner().getId().equals(userId)) {
            throw new UnauthorizedCompanyAccessException("This company is not authorized to view its projects.");
        }

        List<Project> projects = projectRepository.findProjectsByCompanyId(companyId);

        if (projects == null || projects.isEmpty()) {
            throw new ProjectNotFoundException("There are no projects belonging to this company with id: " + companyId);
        }

        List<ProjectInfoDto> projectInfoDtoList = projects.stream()
                .map(project -> {
                    ProjectInfoDto projectInfoDto = new ProjectInfoDto();
                    projectInfoDto.setId(project.getId());
                    projectInfoDto.setName(project.getName());
                    projectInfoDto.setDescription(project.getDescription());
                    projectInfoDto.setCreatedDate(project.getCreatedDate());
                    projectInfoDto.setModifiedDate(project.getModifiedDate());
                    projectInfoDto.setCompanyId(project.getCompany().getId());
                    projectInfoDto.setCompanyName(company.getCompanyName());
                    return projectInfoDto;
                }).collect(Collectors.toList());

        CompanyProjectDto companyProjectDto = new CompanyProjectDto();
        companyProjectDto.setCompanyName(company.getCompanyName());
        companyProjectDto.setProjectInfoDtos(projectInfoDtoList);

        return companyProjectDto;
    }


    @Override
    public CompanyEmployeeDto getCompanyEmployeesByCompanyId(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        if (!company.getCompanyOwner().getId().equals(userId)) {
            throw new UnauthorizedCompanyAccessException("This company is not authorized to view its employees.");
        }

        List<User> users = userRepository.findUsersByCompanyId(companyId);

        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("There are no employees of this company with id: " + companyId);
        }

        List<UserDto> userDtoList = users.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFullName(user.getName() + " " + user.getSurname());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            userDto.setCompanyRoles(user.getCompanyRole());
            userDto.setCompanyId(user.getCompany().getId());
            userDto.setCompanyName(user.getCompany().getCompanyName());
            return userDto;
        }).collect(Collectors.toList());

        CompanyEmployeeDto companyEmployeeDto = new CompanyEmployeeDto();
        companyEmployeeDto.setCompanyName(company.getCompanyName());
        companyEmployeeDto.setUserDtos(userDtoList);

        return companyEmployeeDto;
    }


    @Override
    public CompanyInfoDto approveCompany(Long companyId) { // General Admin
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with id :" + companyId));

        company.setIsActivate(true);
        User companyOwnAdmin = company.getUsers().get(0);

        userService.updateUserRole(companyOwnAdmin.getId(), UserRole.COMPANY_ADMIN);
        companyRepository.save(company);

        return company.getCompanyInfoDto();
    }


    @Override
    public CompanyInfoDto updateCompany(Long companyId, Long userId, CompanyInfoDto companyInfoDto) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        if (!company.getCompanyOwner().getId().equals(userId)) {
            throw new UnauthorizedCompanyAccessException("No access to update this company with id: " + companyId);
        }

        company.setCompanyName(companyInfoDto.getCompanyName());
        company.setModifiedDate(LocalDateTime.now());

        Company updatedCompany = companyRepository.save(company);
        return updatedCompany.getCompanyInfoDto();
    }


    @Override
    public CompanyEmployeeDto addEmployeeForCompany(Long companyId, Long userId, Long companyAdminId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (company.getUsers().contains(user)) {
            throw new UserAlreadyWorksException("User already works in this company: " + company.getCompanyName());
        }

        if (!company.getCompanyOwner().getId().equals(companyAdminId)) {
            throw new UnauthorizedCompanyAccessException("The user does not have the authority to add personnel to this company.");
        }

        user.setCompany(company);
        userRepository.save(user);

        CompanyEmployeeDto companyEmployeeDto = new CompanyEmployeeDto();
        companyEmployeeDto.setCompanyName(user.getCompany().getCompanyName());

        List<UserDto> userDtos = company.getUsers().stream()
                .map(User::getDto)
                .collect(Collectors.toList());
        companyEmployeeDto.setUserDtos(userDtos);

        return companyEmployeeDto;
    }


    @Override
    public CompanyEmployeeDto removeEmployeeForCompany(Long companyId, Long userId, Long companyAdminId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!company.getUsers().contains(user)) {
            throw new UserNotInCompanyException("User is not an employee of this company: " + company.getCompanyName());
        }

        if (!company.getCompanyOwner().getId().equals(companyAdminId)) {
            throw new UnauthorizedCompanyAccessException("The user does not have the authority to remove personnel from this company.");
        }

        user.setCompany(null);
        userRepository.save(user);

        Company updatedCompany = companyRepository.save(company);

        CompanyEmployeeDto companyEmployeeDto = new CompanyEmployeeDto();

        List<UserDto> userDtos = updatedCompany.getUsers().stream()
                .map(User::getDto)
                .collect(Collectors.toList());
        companyEmployeeDto.setUserDtos(userDtos);

        return companyEmployeeDto;
    }


    @Override
    public CompanyInfoDto createCompany(Long userId, CompanyInfoDto companyInfoDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));


        if (user.getRole() != UserRole.USER) {
            throw new IllegalArgumentException("Only users can create companies.");
        }

        Company company = new Company();
        company.setCompanyName(companyInfoDto.getCompanyName());
        company.setCreatedDate(LocalDateTime.now());
        company.setModifiedDate(LocalDateTime.now());
        company.setIsActivate(false);
        company.setCompanyOwner(user);

        Company savedCompany = companyRepository.save(company);

        addEmployeeForCompany(savedCompany.getId(), userId, userId);

        user.setCompany(company);
        user.getCompanyRole().add(CompanyRole.COMPANY_OWN);

        return savedCompany.getCompanyInfoDto();
    }


    @Override
    public boolean deleteCompany(Long companyId, Long companyAdminId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        if (!company.getCompanyOwner().getId().equals(companyAdminId)) {
            throw new UnauthorizedCompanyAccessException("No authority to remove this company.");
        }

        List<User> users = userRepository.findUsersByCompanyId(company.getId());
        for (User user : users) {
            user.setCompany(null);
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
        companyRepository.deleteById(companyId);
        return true;
    }
}
