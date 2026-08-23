package com.qapipeline.dto;

import com.qapipeline.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve possuir no máximo 100 caracteres")
    private String title;

    @Size(max = 500, message = "A descrição deve possuir no máximo 500 caracteres")
    private String description;

    @NotNull(message = "A prioridade é obrigatória")
    private TaskPriority priority;

    public TaskUpdateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}