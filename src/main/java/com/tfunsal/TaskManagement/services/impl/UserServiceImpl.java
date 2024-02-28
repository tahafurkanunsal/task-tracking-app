package com.tfunsal.TaskManagement.services.impl;

import com.tfunsal.TaskManagement.dto.*;
import com.tfunsal.TaskManagement.entities.Project;
import com.tfunsal.TaskManagement.entities.Task;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.CompanyRole;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.UserRole;
import com.tfunsal.TaskManagement.exception.*;
import com.tfunsal.TaskManagement.repository.ProjectRepository;
import com.tfunsal.TaskManagement.repository.TaskRepository;
import com.tfunsal.TaskManagement.repository.UserRepository;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getDto).collect(Collectors.toList());
    }

    @Override
    public UserUpdateDto updateUserRole(Long userId , UserRole role){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setRole(role);
        User updateUser = userRepository.save(user);

        return updateUser.getUserUpdateDto();

    }

    @Override
    public UserUpdateDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getSurname() != null) {
            user.setSurname(userUpdateDto.getSurname());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }

        User updatedUser = userRepository.save(user);

        return updatedUser.getUserUpdateDto();
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }
}
