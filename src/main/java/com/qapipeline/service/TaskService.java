package com.qapipeline.service;

import com.qapipeline.dto.TaskCreateRequest;
import com.qapipeline.model.Task;
import com.qapipeline.model.TaskStatus;
import com.qapipeline.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.qapipeline.exception.TaskNotFoundException;
import com.qapipeline.dto.TaskUpdateRequest;
import com.qapipeline.dto.TaskStatusUpdateRequest;

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

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task update(Long id, TaskUpdateRequest request) {

        Task task = findById(id);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());

        return taskRepository.save(task);
    }

    public Task updateStatus(Long id, TaskStatusUpdateRequest request) {

        Task task = findById(id);

        task.setStatus(request.getStatus());

        return taskRepository.save(task);
    }

    public void delete(Long id) {

        Task task = findById(id);

        taskRepository.delete(task);
    }
}