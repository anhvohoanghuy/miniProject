package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "startDate",nullable = false)
    private LocalDate startDate;
    @Column(name = "endDate",nullable = false)
    private LocalDate endDate;
    @Column(name = "status",nullable = false)
    private Integer status;
    @Column(name = "priority",nullable = false)// 1: chưa bắt đầu, 2: đang thực hiện, 3: tạm dừng, 4: hoàn thành, 5: quá hạn
    private Integer priority;
    @Column(name = "user_id",nullable = false)
    private UUID userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",insertable = false,updatable = false)
    public User user;
}
