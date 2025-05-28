package com.example.demo.repository;

import com.example.demo.model.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogRepository extends JpaRepository<TaskLog,String> {
}
