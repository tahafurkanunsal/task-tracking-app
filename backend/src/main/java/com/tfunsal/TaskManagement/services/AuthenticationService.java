package com.tfunsal.TaskManagement.services;

import com.tfunsal.TaskManagement.dto.JwtAuthenticationResponse;
import com.tfunsal.TaskManagement.dto.RefreshTokenRequest;
import com.tfunsal.TaskManagement.dto.SignInRequest;
import com.tfunsal.TaskManagement.dto.SignUpRequest;
import com.tfunsal.TaskManagement.entities.User;

public interface AuthenticationService {

    User signUp(SignUpRequest signUpRequest);

//    JwtAuthenticationResponse signIn(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}