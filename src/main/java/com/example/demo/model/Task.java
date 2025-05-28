package com.example.demo.model;

import com.example.demo.dto.TaskDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
public class Task implements Serializable {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "priority", nullable = false)
// 1: chưa bắt đầu, 2: đang thực hiện, 3: tạm dừng, 4: hoàn thành, 5: quá hạn
    private Integer priority;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    public User user;

    public Task(TaskDto taskDto) {
        this.title = taskDto.getTitle();
        this.description = taskDto.getDescription();
        this.startDate = taskDto.getStartDate();
        this.endDate = taskDto.getEndDate();
        this.status = taskDto.getStatus();
        this.priority = taskDto.getPriority();
    }

    private Task(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.priority = builder.priority;
        this.userId = builder.userId;
    }

    public static class Builder {
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer priority;
        private String userId;


        public Builder(String title) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Title không được để trống");
            }
            this.title = title;
        }

        private Builder description(String description) {
            this.description = description;
            return this;
        }

        private Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        private Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        private Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        private Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        private Task build() {
            return new Task(this);
        }
    }
}
