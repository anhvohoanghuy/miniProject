package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="user_claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name="user_id",nullable = false)
    public UUID userId;
    @Column(name="claim_id",nullable = false)
    public int claimId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="claim_id",insertable = false,updatable = false)
    @JsonBackReference
    public Claim claim;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",insertable = false,updatable = false)
    @JsonBackReference
    public User user;
}
