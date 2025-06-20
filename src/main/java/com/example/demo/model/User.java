package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"tasks","userClaims" })
public class User implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @Column(name="username",nullable = false,unique = true)
    private String username;
    @Column(name="email",nullable = false,unique = true)
    private String email;
    @Column(name="password",nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UserClaim> userClaims;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Task> tasks;
}
