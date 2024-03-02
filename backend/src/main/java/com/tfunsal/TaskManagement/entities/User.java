package com.tfunsal.TaskManagement.entities;

import com.tfunsal.TaskManagement.dto.UserDto;
import com.tfunsal.TaskManagement.dto.UserUpdateDto;
import com.tfunsal.TaskManagement.enums.CompanyRole;
import com.tfunsal.TaskManagement.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private UserRole role;

    private List<CompanyRole> companyRole = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDto getDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFullName(name + " " + surname);
        userDto.setRole(role);
        List<CompanyRole> companyRoles = new ArrayList<>();
        for (CompanyRole cRole : companyRole) {
            companyRoles.add(cRole);
        }
        userDto.setCompanyRoles(companyRoles);
        userDto.setEmail(email);
        if (company != null) {
            userDto.setCompanyName(company.getCompanyName());
            userDto.setCompanyId(company.getId());
        } else {
            userDto.setCompanyName(null);
            userDto.setCompanyId(null);
        }
        return userDto;
    }

    public UserUpdateDto getUserUpdateDto() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(id);
        userUpdateDto.setName(name);
        userUpdateDto.setSurname(surname);
        userUpdateDto.setEmail(email);
        userUpdateDto.setRole(role);

        return userUpdateDto;
    }
}