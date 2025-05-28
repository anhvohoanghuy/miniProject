package com.example.demo.service.KafkaService;

import com.example.demo.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatKafkaProducer {
    @Autowired
    private KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void sendToPrivate(MessageDto message) {
        kafkaTemplate.send("private-messages", message.getReceiver(), message);
    }

}
