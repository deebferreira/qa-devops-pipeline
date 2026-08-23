package com.qapipeline.service;

import com.qapipeline.dto.TaskCreateRequest;
import com.qapipeline.model.Task;
import com.qapipeline.model.TaskStatus;
import com.qapipeline.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(TaskCreateRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.TODO);

        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}