package com.tfunsal.TaskManagement.repository;

import com.tfunsal.TaskManagement.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByTaskId(Long taskId);

}