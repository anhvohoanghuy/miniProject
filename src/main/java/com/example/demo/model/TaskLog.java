package com.example.demo.model;

import com.example.demo.dto.LogDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name="log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskLog implements Serializable {
    @Id
    @UuidGenerator
    private String id;
    private String description;
    private LocalDateTime time;
    public TaskLog(LogDto logDto){
        this.description=logDto.getDescription();
        this.time = logDto.getTime();
    }
}
