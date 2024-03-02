package com.tfunsal.TaskManagement.config;

import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.services.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/companies").permitAll()
                        .requestMatchers("/api/app/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/users").hasAuthority("ADMIN")
                        .requestMatchers("/api/companies/**").hasAuthority("COMPANY_ADMIN")
                        .requestMatchers("/api/comments").hasAuthority("COMPANY_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/{taskId}/status").hasAuthority("USER")
                        .requestMatchers(
                                "/api/projects/{projectId}/tasks",
                                "/api/projects/{projectId}/tasks/{taskId}/details",
                                "/api/projects/{projectId}/tasks/{taskId}/assigned-users",
                                "/api/projects/{projectId}/tasks/{taskId}/assignee/{userId}",
                                "/api/projects/{projectId}/tasks/{taskId}/unassignee/{userId}",
                                "/api/projects/{projectId}/tasks/{taskId}").hasAuthority("COMPANY_ADMIN")
                        .requestMatchers(
                                "/api/tasks",
                                "/api/users/{userId}/all-tasks",
                                "/api/projects/{projectId}/users/{userId}/tasks",
                                "/api/projects/{projectId}/tasks/{taskId}/comments/**",
                                "/api/users/{userId}").hasAnyAuthority(UserRole.COMPANY_ADMIN.name(), UserRole.USER.name())

                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}