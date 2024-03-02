package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.*;

import java.util.List;

public interface CompanyService {

    List<CompanyInfoDto> getAllCompanies();

    CompanyDto getCompanyDetails(Long companyId, Long userId);

    CompanyDto getCompanyByCompanyName(String companyName);

    List<CompanyInfoDto> getPendingCompanies();

    CompanyInfoDto approveCompany(Long companyId);

    CompanyInfoDto createCompany(Long userId, CompanyInfoDto companyInfoDto);

    CompanyInfoDto updateCompany(Long companyId, Long userId, CompanyInfoDto companyInfoDto);

    boolean deleteCompany(Long companyId, Long companyAdminId);

    CompanyProjectDto getCompanyProjectsByCompanyId(Long companyId, Long userId);

    CompanyEmployeeDto getCompanyEmployeesByCompanyId(Long companyId, Long userId);

    CompanyEmployeeDto addEmployeeForCompany(Long companyId, Long userId, Long companyAdminId);

    CompanyEmployeeDto removeEmployeeForCompany(Long companyId, Long userId, Long companyAdminId);

}
