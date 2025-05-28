package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Claim implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name="claim_type",nullable = false)
    public String claimType;
    @Column(name="claim_value",nullable = false)
    public String claimValue;
    @OneToMany(mappedBy = "claim")
    @JsonManagedReference
    public Set<UserClaim> userClaims;
}
