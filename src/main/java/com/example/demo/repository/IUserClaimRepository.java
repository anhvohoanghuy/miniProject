package com.example.demo.repository;

import com.example.demo.model.UserClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IUserClaimRepository extends JpaRepository<UserClaim, Integer> {
    List<UserClaim> findByUserId(UUID userId);
}
