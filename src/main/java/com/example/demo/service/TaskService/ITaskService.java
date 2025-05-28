package com.example.demo.service.TaskService;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;

import java.util.List;

public interface ITaskService {
    Task createTask(TaskDto model);
    Task updateTask(String id, TaskDto taskDetails);
    void deleteTask(String id);
    List<TaskDto> getAllTasks(String userId);
    TaskDto getTask(String id);
    Task updateStatus(String id, Integer newStatus);
    void autoUpdateStatus();
}
