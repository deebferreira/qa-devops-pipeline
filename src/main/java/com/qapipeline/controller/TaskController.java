package com.qapipeline.controller;

import com.qapipeline.dto.TaskCreateRequest;
import com.qapipeline.model.Task;
import com.qapipeline.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.qapipeline.dto.TaskUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> create(
            @Valid @RequestBody TaskCreateRequest request) {

        Task task = taskService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAll() {

        List<Task> tasks = taskService.findAll();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {

        Task task = taskService.findById(id);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {

        Task task = taskService.update(id, request);

        return ResponseEntity.ok(task);
    }
}