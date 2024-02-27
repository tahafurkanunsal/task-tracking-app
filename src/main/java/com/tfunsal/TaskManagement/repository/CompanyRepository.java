package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyByCompanyName(String companyName);

    Company findCompanyByCompanyOwnerId(Long userId);

    Company findCompanyByIdAndCompanyOwnerId(Long companyId, Long userId);

    List<Company> findCompanyByIsActivate(boolean isActive);

}