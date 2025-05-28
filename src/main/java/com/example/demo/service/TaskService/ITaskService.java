package com.example.demo.service.TaskService;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ITaskService {
    CompletableFuture<Task> createTask(TaskDto model);
    CompletableFuture<Task> updateTask(String id, TaskDto taskDetails);
    CompletableFuture<Void> deleteTask(String id);
    CompletableFuture<List<Task>> getAllTasks(String userId);
    CompletableFuture<Task> getTask(String id);
    CompletableFuture<Task> updateStatus(String id, Integer newStatus);
    CompletableFuture<Void> autoUpdateStatus();
}
