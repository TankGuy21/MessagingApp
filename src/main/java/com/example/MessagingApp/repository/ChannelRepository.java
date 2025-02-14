package com.example.MessagingApp.repository;
import com.example.MessagingApp.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
