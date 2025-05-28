package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;
    @Async
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResponse<Task>>> createTask(@RequestBody TaskDto task) {
        return taskService.createTask(task).thenApply(taskResponse-> ResponseEntity.ok(ApiResponse.ok(taskResponse)));
    }
@Async
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Task>>> updateTask(@PathVariable String id, @RequestBody TaskDto task) {
        return taskService.updateTask(id,task).thenApply(response-> ResponseEntity.ok(ApiResponse.ok(response)));
    }
    @Async
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteTask(id).thenApply(response->ResponseEntity.ok().build());
    }

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<ApiResponse<List<Task>>>> getAllTasks(
            @RequestParam String userId) {
        return taskService.getAllTasks(userId).thenApply(response-> ResponseEntity.ok(ApiResponse.ok(response)));
    }

    @Async
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Task>>> getTask(@PathVariable String id) {
        return taskService.getTask(id).thenApply(response-> ResponseEntity.ok(ApiResponse.ok(response)));
    }

    @Async
    @PatchMapping("/{id}/status")
    public CompletableFuture<ResponseEntity<ApiResponse<Task>>> updateStatus(@PathVariable String id, @RequestParam Integer status) {
        return taskService.updateStatus(id, status).thenApply(response->ResponseEntity.ok(ApiResponse.ok(response)));
    }

    @Async
    // Auto update trạng thái
    @PostMapping("/update-status")
    public CompletableFuture<ResponseEntity<Void>> autoUpdateStatus() {
        return taskService.autoUpdateStatus().thenApply(response->ResponseEntity.ok().build());
    }
}
