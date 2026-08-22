package com.qapipeline.model;

import java.time.LocalDateTime;

public class Task {

    private Long id;

    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}