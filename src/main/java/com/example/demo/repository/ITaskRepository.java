package com.example.demo.repository;

import com.example.demo.model.Task;
import com.sun.jdi.InterfaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ITaskRepository extends JpaRepository<Task, String> {
    List<Task> findByTitleContainingIgnoreCase(String keyword);
    List<Task> findByStatus(Integer status);
}
