package com.tfunsal.TaskManagement.services;


import com.tfunsal.TaskManagement.dto.UserUpdateDto;
import com.tfunsal.TaskManagement.enums.UserRole;

public interface UserService {

    UserUpdateDto updateUserRole(Long userId, UserRole newRole);

//    UserUpdateDto updateUser(Long userId , UserUpdateDto userUpdateDto);
//
//    boolean deleteUser(Long userId);


}
