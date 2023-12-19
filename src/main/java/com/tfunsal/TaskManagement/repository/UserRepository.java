package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.User;
import com.tfunsal.TaskManagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByRole(UserRole role);
}
