package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    @Async
    CompletableFuture<Optional<User>> findByUsername(String username);
    @Async
    CompletableFuture<Boolean> existsByEmail(String email);
    @Async
    CompletableFuture<Boolean> existsByUsername(String username);
}
