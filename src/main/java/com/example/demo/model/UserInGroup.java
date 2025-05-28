package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.kafka.common.protocol.types.Field;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Table(name = "userInGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInGroup implements Serializable {
    @Id
    @UuidGenerator
    private String Id;
    @Column(nullable = false)
    private String groupId;
    @Column(nullable = false)
    private String UserId;
}
