package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;
import com.example.demo.repository.ITaskRepository;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody TaskDto task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @RequestParam String userId) {
        return ResponseEntity.ok(taskService.getAllTasks(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable String id, @RequestParam Integer status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    // Auto update trạng thái
    @PostMapping("/update-status")
    public ResponseEntity<Void> autoUpdateStatus() {
        taskService.autoUpdateStatus();
        return ResponseEntity.ok().build();
    }
}
