package com.example.MessagingApp.repository;
import com.example.MessagingApp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverId(Long receiverId);
    List<Message> findByChannelId(Long channelId);
}
