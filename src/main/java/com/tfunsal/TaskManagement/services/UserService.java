package com.tfunsal.TaskManagement.services;


import com.tfunsal.TaskManagement.dto.UserDto;
import com.tfunsal.TaskManagement.dto.UserUpdateDto;
import com.tfunsal.TaskManagement.enums.UserRole;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserUpdateDto updateUserRole(Long userId, UserRole newRole);

    UserUpdateDto updateUser(Long userId , UserUpdateDto userUpdateDto);

    boolean deleteUser(Long userId);


}
