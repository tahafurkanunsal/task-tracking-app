package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.JwtAuthenticationResponse;
import com.tfunsal.TaskManagement.dto.RefreshTokenRequest;
import com.tfunsal.TaskManagement.dto.SignUpRequest;
import com.tfunsal.TaskManagement.entities.Company;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.repository.CompanyRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.AuthenticationService;

import com.tfunsal.TaskManagement.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User signUp(SignUpRequest signUpRequest) {

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setSurname(signUpRequest.getSurname());
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setCompanyRole(new ArrayList<>());
        user.setCompany(null);

        return userRepository.save(user);
    }

/*    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password!"));
        var jwt = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }*/


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtUtil.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtUtil.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtUtil.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }
        return null;
    }

    @PostConstruct
    public void createAdminAccount() {

        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (adminAccount == null) {

            Company company = new Company();
            company.setCompanyName("COMPANY-PROJECT-APP");
            companyRepository.save(company);

            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setSurname("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setCompany(company);

            userRepository.save(user);
        }
    }
}