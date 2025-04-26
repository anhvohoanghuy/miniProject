package com.example.demo.repository;

import com.example.demo.model.Claim;
import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClaimRepository extends JpaRepository<Claim, Integer> {
}
