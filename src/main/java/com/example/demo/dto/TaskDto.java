package com.example.demo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private  UUID id;

    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime  endDate;

    private Integer status;
    // 1: chưa bắt đầu, 2: đang thực hiện, 3: tạm dừng, 4: hoàn thành, 5: quá hạn
    private Integer priority;

    private UUID userId;
}
