package com.example.demo.model;

import com.example.demo.dto.MessageDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "message")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String sender;
    private String receiver;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Message(MessageDto messageDto){
        this.sender =messageDto.getSender();
        this.receiver=messageDto.getReceiver();
        this.content= messageDto.getContent();
        this.timestamp=messageDto.getTimestamp();
    }
}
