package com.example.MessagingApp.repository;
import com.example.MessagingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
