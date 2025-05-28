package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.model.Message;
import com.example.demo.repository.IMessageRepository;
import com.example.demo.service.KafkaService.ChatKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/message")
@RequiredArgsConstructor
class MessageController {
    private ChatKafkaProducer kafkaProducer;
    private SimpMessagingTemplate messagingTemplate;
    private IMessageRepository messageRepository;
    @MessageMapping("/send-private") // /app/send-private
    public void sendPrivateMessage(MessageDto message) {
        Message messageToSend = new Message(message);
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/private", messageToSend);
    }

    @GetMapping("/history")
    public List<Message> getChatHistory(@RequestParam String user1, @RequestParam String user2) {
        return messageRepository.findChatMessagesBetweenUsers(user1, user2);
    }
}
