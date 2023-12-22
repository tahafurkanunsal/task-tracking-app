package com.tfunsal.TaskManagement.controller;

import com.tfunsal.TaskManagement.dto.TaskDto;
import com.tfunsal.TaskManagement.enums.TaskStatus;
import com.tfunsal.TaskManagement.enums.TaskTag;
import com.tfunsal.TaskManagement.exception.NoSuchTaskExistsException;
import com.tfunsal.TaskManagement.exception.UserNotFoundException;
import com.tfunsal.TaskManagement.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasksDtoList = adminService.getAllTasks();
        return ResponseEntity.ok(tasksDtoList);
    }

    @GetMapping(value = "/tasks", params = {"taskId"})
    public ResponseEntity<TaskDto> getTaskByTaskId(@RequestParam Long taskId) {
        TaskDto taskDto = adminService.getTaskByTaskId(taskId);
        return ResponseEntity.ok(taskDto);
    }

    @GetMapping(value = "/tasks", params = {"userId"})
    public ResponseEntity<List<TaskDto>> getTaskByUserId(@RequestParam Long userId) {
        List<TaskDto> taskDtoList = adminService.getTasksByUserId(userId);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"tag"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskTag(@RequestParam TaskTag tag) {
        List<TaskDto> taskDtoList = adminService.getTasksByTaskTag(tag);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"status"})
    public ResponseEntity<List<TaskDto>> getTasksByTaskStatus(@RequestParam TaskStatus status) {
        List<TaskDto> taskDtoList = adminService.getTasksByTaskStatus(status);
        return ResponseEntity.ok(taskDtoList);
    }

    @GetMapping(value = "/tasks", params = {"startDate", "endDate"})
    public ResponseEntity<List<TaskDto>> getTasksByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<TaskDto> taskDtoList = adminService.getTasksByDate(startDate, endDate);
        return ResponseEntity.ok(taskDtoList);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        TaskDto newTaskDto = adminService.create(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDto);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> update(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {

        try {
            TaskDto updatedTask = adminService.update(taskId, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (NoSuchTaskExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/tasks/{taskId}/assignee/{userId}")
    public ResponseEntity<TaskDto> assignAUserForTask(@PathVariable Long taskId,
                                                      @PathVariable Long userId) {
        TaskDto assignedTask = adminService.assignAUserForTask(taskId, userId);

        if (assignedTask != null) {
            return new ResponseEntity<>(assignedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable Long taskId) {

        boolean deleted = adminService.delete(taskId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}