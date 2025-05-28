package com.example.demo.repository;

import com.example.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message,String> {
    @Query("SELECT c FROM ChatMessage c WHERE " +
            "(c.sender = :user1 AND c.receiver = :user2) " +
            "OR (c.sender = :user2 AND c.receiver = :user1) " +
            "ORDER BY c.timestamp ASC")
    List<Message> findChatMessagesBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);
}
