package com.example.demo.service.RabbitMQService;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.model.TaskLog;
import com.example.demo.repository.ITaskLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogConsumer {
    private final ITaskLogRepository taskLogRepository;
    @RabbitListener(queues = RabbitMQConfig.QUEUE,containerFactory = "rabbitListenerContainerFactory")
    public void receiveTaskLog(TaskLog taskLog){
        taskLogRepository.save(taskLog);
        System.out.println(taskLog.getDescription());
    }
}
