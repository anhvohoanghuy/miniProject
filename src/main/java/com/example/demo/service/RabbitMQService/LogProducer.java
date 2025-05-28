package com.example.demo.service.RabbitMQService;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.model.TaskLog;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LogProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendTaskLog(TaskLog log){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                log
        );
    }
}
