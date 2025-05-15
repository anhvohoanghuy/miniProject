package com.example.demo.service;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;
import com.example.demo.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    public Task createTask(TaskDto model) {
        Task task = new Task();
        task.setTitle(model.getTitle());
        task.setDescription(model.getDescription());
        task.setStartDate(model.getStartDate());
        task.setEndDate(model.getEndDate());
        task.setPriority(model.getPriority());
        task.setStatus(1); // Mặc định là chưa bắt đầu
        task.setUserId(model.getUserId());
        return taskRepository.save(task);
    }

    public Task updateTask(String id, TaskDto taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()){
            return null;
        }
        Task task = taskOptional.get();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStartDate(taskDetails.getStartDate());
        task.setEndDate(taskDetails.getEndDate());
        task.setPriority(taskDetails.getPriority());
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    public List<TaskDto> getAllTasks(String userId) {
        List<Task> tasks = taskRepository.findAll().stream().filter(task -> Objects.equals(task.getUserId().toString(), userId))
                .toList();

        return tasks.stream()
                .map(task ->
            new TaskDto(task.getId(),task.getTitle(), task.getDescription(), task.getStartDate(), task.getEndDate(), task.getStatus(), task.getPriority(), task.getUserId()))
                .collect(Collectors.toList());
    }
    public TaskDto getTask(String id){
        var task= taskRepository.findById(id);
        if (task.isEmpty()){
            return null;
        }
        var currentTask = task.get();
        return new TaskDto(currentTask.getId(),currentTask.getTitle(),
                currentTask.getDescription(),currentTask.getStartDate(),
                currentTask.getEndDate(),currentTask.getStatus(),currentTask.getPriority(),
                currentTask.getUserId());
    }
    public Task updateStatus(String id, Integer newStatus) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty()){
            System.out.println("task null");
            return null;
        }
        Task tasks = task.get();
        tasks.setStatus(newStatus);
        return taskRepository.save(tasks);
    }
    @Scheduled(cron = "0 0 1 * * *")
    public void autoUpdateStatus() {
        List<Task> tasks = taskRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (task.getEndDate() != null && LocalDateTime.now().isAfter(task.getEndDate())) {
                task.setStatus(5); // quá hạn
            } else if (task.getStartDate() != null && LocalDateTime.now().isBefore(task.getStartDate())) {
                task.setStatus(1); // chưa bắt đầu
            }
            taskRepository.save(task);
        }
    }
}
