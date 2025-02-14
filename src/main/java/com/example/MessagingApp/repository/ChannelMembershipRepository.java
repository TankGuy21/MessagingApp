package com.example.MessagingApp.repository;
import com.example.MessagingApp.model.Channel;
import com.example.MessagingApp.model.ChannelMembership;
import com.example.MessagingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, Long> {
    Optional<ChannelMembership> findByChannelAndUser(Channel channel, User user);
}
