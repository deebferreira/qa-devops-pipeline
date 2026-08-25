package com.qapipeline.dto;

import com.qapipeline.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class TaskStatusUpdateRequest {

    @NotNull(message = "O status é obrigatório")
    private TaskStatus status;

    public TaskStatusUpdateRequest() {
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}