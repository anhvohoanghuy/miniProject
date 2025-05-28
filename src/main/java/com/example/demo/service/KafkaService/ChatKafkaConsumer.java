package com.example.demo.service.KafkaService;

import com.example.demo.dto.MessageDto;
import com.example.demo.model.Message;
import com.example.demo.repository.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatKafkaConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IMessageRepository messageRepository;

    @KafkaListener(topics = "private-messages", groupId = "chat-app",concurrency = "3")
    public void handlePrivate(MessageDto message) {
        // Lưu DB ở đây
        Message message1= new Message(message);
        messageRepository.save(message1);
        // Gửi lại cho người nhận
        messagingTemplate.convertAndSend("/topic/chat/private/" + message.getReceiver(), message);
    }
}
