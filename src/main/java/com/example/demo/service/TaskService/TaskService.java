package com.example.demo.service.TaskService;

import com.example.demo.dto.LogDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorEnum;
import com.example.demo.model.Task;
import com.example.demo.model.TaskLog;
import com.example.demo.repository.ITaskRepository;
import com.example.demo.service.RabbitMQService.LogProducer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class TaskService implements ITaskService {
    private final ITaskRepository taskRepository;
    private final LogProducer logProducer;

    @Async
    public CompletableFuture<Task> createTask(TaskDto model) {

        Task task = new Task();
        task.setTitle(model.getTitle());
        task.setDescription(model.getDescription());
        task.setStartDate(model.getStartDate());
        task.setEndDate(model.getEndDate());
        task.setPriority(model.getPriority());
        task.setStatus(1); // Mặc định là chưa bắt đầu
        task.setUserId(model.getUserId());
        LogDto logDto = new LogDto(model.getUserId() + " Create " + model.getTitle(), LocalDateTime.now());
        TaskLog taskLog = new TaskLog(logDto);
        logProducer.sendTaskLog(taskLog);
        Task saveTask = taskRepository.save(task);
        return CompletableFuture.completedFuture(saveTask);
    }

    public CompletableFuture<Task> updateTask(String id, TaskDto taskDetails) {
        Task task = update(id, taskDetails);
        LogDto logDto = new LogDto(taskDetails.getUserId() + " Create " + taskDetails.getTitle(), LocalDateTime.now());
        TaskLog taskLog = new TaskLog(logDto);
        logProducer.sendTaskLog(taskLog);
        return CompletableFuture.completedFuture(task);
    }
    @Async
    public CompletableFuture<Void> deleteTask(String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new CustomException(ErrorEnum.TASK_NOT_FOUND));
        taskRepository.delete(task);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Task>> getAllTasks(String userId) {
        List<Task> tasks = taskRepository.findAll().stream().filter(task -> Objects.equals(task.getUserId(), userId))
                .toList();

        return CompletableFuture.completedFuture(tasks);
    }

    public CompletableFuture<Task> getTask(String id) {
        var task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new CustomException(ErrorEnum.TASK_NOT_FOUND);
        }
        var currentTask = task.get();
        return CompletableFuture.completedFuture(currentTask);
    }

    public CompletableFuture<Task> updateStatus(String id, Integer newStatus) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            System.out.println("task null");
            throw new CustomException(ErrorEnum.TASK_NOT_FOUND);
        }
        Task tasks = task.get();
        tasks.setStatus(newStatus);
        return CompletableFuture.completedFuture(taskRepository.save(tasks));
    }

    @Scheduled(cron = "0 0 1 * * *")
    public CompletableFuture<Void> autoUpdateStatus() {
        List<Task> tasks = taskRepository.findAll();
        LocalDateTime today = LocalDateTime.now();

        for (Task task : tasks) {
            if (task.getEndDate() != null && today.isAfter(task.getEndDate())) {
                task.setStatus(5); // quá hạn
            } else if (task.getStartDate() != null && today.isBefore(task.getStartDate())) {
                task.setStatus(1); // chưa bắt đầu
            }
            taskRepository.save(task);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public Task update(String id, TaskDto update) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new CustomException(ErrorEnum.USER_NOT_FOUND);
        }
        Task task = taskOptional.get();
        task.setTitle(update.getTitle());
        task.setDescription(update.getDescription());
        task.setStartDate(update.getStartDate());
        task.setEndDate(update.getEndDate());
        task.setPriority(update.getPriority());
        task.setStatus(update.getStatus());
        return task;
    }
}
