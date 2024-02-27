package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.CompanyDto;
import com.tfunsal.TaskManagement.dto.CompanyEmployeeDto;
import com.tfunsal.TaskManagement.dto.CompanyInfoDto;
import com.tfunsal.TaskManagement.dto.CompanyProjectDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/app/companies")
    public ResponseEntity<List<CompanyInfoDto>> getAllCompanies() {
        List<CompanyInfoDto> companyInfoDtoList = companyService.getAllCompanies();
        return ResponseEntity.ok(companyInfoDtoList);
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyDto> getCompanyDetails(@PathVariable Long companyId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CompanyDto companyDto = companyService.getCompanyDetails(companyId, user.getId());
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping(value = "/app/companies", params = {"companyName"})
    public ResponseEntity<CompanyDto> getCompanyByCompanyName(@RequestParam String companyName) {
        CompanyDto companyDto = companyService.getCompanyByCompanyName(companyName);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/app/companies/pending-companies")
    public ResponseEntity<List<CompanyInfoDto>> getPendingCompanies() {
        List<CompanyInfoDto> companyInfoDtoList = companyService.getPendingCompanies();
        return ResponseEntity.ok(companyInfoDtoList);
    }

    @GetMapping("/companies/{companyId}/projects")
    public ResponseEntity<CompanyProjectDto> getCompanyProjectsByCompanyId(@PathVariable Long companyId,
                                                                           Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyProjectDto companyProjectDto = companyService.getCompanyProjectsByCompanyId(companyId, user.getId());
            return ResponseEntity.ok(companyProjectDto);
        } catch (ProjectNotFoundException | CompanyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/companies/{companyId}/employees")
    public ResponseEntity<CompanyEmployeeDto> getCompanyEmployeesByCompanyId(@PathVariable Long companyId,
                                                                             Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyEmployeeDto companyEmployeeDto = companyService.getCompanyEmployeesByCompanyId(companyId, user.getId());
            return ResponseEntity.ok(companyEmployeeDto);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyInfoDto> createCompany(@RequestBody CompanyInfoDto companyInfoDto,
                                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyInfoDto newCompany = companyService.createCompany(user.getId(), companyInfoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PatchMapping(value = "/companies/{companyId}/add-employee/{userId}")
    public ResponseEntity<CompanyEmployeeDto> addEmployeeForCompany(@PathVariable Long companyId,
                                                                    @PathVariable Long userId,
                                                                    Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyEmployeeDto companyEmployeeDto = companyService.addEmployeeForCompany(companyId, userId, user.getId());
            return ResponseEntity.ok(companyEmployeeDto);
        } catch (CompanyNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyWorksException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/companies/{companyId}/remove-employee/{userId}")
    public ResponseEntity<CompanyEmployeeDto> removeEmployeeForCompany(@PathVariable Long companyId,
                                                                       @PathVariable Long userId,
                                                                       Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyEmployeeDto companyEmployeeDto = companyService.removeEmployeeForCompany(companyId, userId, user.getId());
            return ResponseEntity.ok(companyEmployeeDto);
        } catch (CompanyNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotInCompanyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/app/companies/{companyId}/approve")
    public ResponseEntity<CompanyInfoDto> approveCompany(@PathVariable Long companyId) {
        try {
            CompanyInfoDto approvedCompany = companyService.approveCompany(companyId);
            return ResponseEntity.ok(approvedCompany);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/companies/{companyId}")
    public ResponseEntity<CompanyInfoDto> updateCompany(@PathVariable Long companyId,
                                                        @RequestBody CompanyInfoDto companyInfoDto,
                                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            CompanyInfoDto updatedCompany = companyService.updateCompany(companyId, user.getId(), companyInfoDto);
            return ResponseEntity.ok(updatedCompany);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            boolean deleted = companyService.deleteCompany(companyId, user.getId());

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (CompanyNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedCompanyAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
