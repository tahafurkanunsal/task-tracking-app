package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.UserDto;
import com.tfunsal.TaskManagement.dto.UserUpdateDto;
import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.exception.UserNotFoundException;
import com.tfunsal.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserUpdateDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        try {
            User authenticatedUser = (User) authentication.getPrincipal();
            if (!authenticatedUser.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            UserUpdateDto updatedUser = userService.updateUser(userId, userUpdateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, Authentication authentication) {
        try {
            User authenticatedUser = (User) authentication.getPrincipal();
            if (!authenticatedUser.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

