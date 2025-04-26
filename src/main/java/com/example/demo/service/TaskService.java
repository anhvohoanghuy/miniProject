package com.example.demo.service;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;
import com.example.demo.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

    public Task updateTask(UUID id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStartDate(taskDetails.getStartDate());
        task.setEndDate(taskDetails.getEndDate());
        task.setPriority(taskDetails.getPriority());
        return taskRepository.save(task);
    }

    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    public List<Task> getAllTasks(String keyword, Integer status, String sortBy) {
        List<Task> tasks = taskRepository.findAll();

        if (keyword != null && !keyword.isEmpty()) {
            tasks = taskRepository.findByTitleContainingIgnoreCase(keyword);
        }

        if (status != null) {
            tasks = tasks.stream().filter(t -> t.getStatus().equals(status)).toList();
        }

        if (sortBy != null && sortBy.equals("priority")) {
            tasks = tasks.stream().sorted(Comparator.comparing(Task::getPriority)).toList();
        }

        return tasks;
    }

    public Task updateStatus(UUID id, Integer newStatus) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }
    @Scheduled(cron = "0 0 2 * * *")
    public void autoUpdateStatus() {
        List<Task> tasks = taskRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (task.getEndDate() != null && today.isAfter(task.getEndDate())) {
                task.setStatus(5); // quá hạn
            } else if (task.getStartDate() != null && today.isAfter(task.getStartDate().minusDays(1)) && today.isBefore(task.getEndDate().plusDays(1))) {
                task.setStatus(2); // đang thực hiện
            } else if (task.getStartDate() != null && today.isBefore(task.getStartDate())) {
                task.setStatus(1); // chưa bắt đầu
            }
            taskRepository.save(task);
        }
    }
}
