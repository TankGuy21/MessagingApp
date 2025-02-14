package com.example.MessagingApp.service;
import com.example.MessagingApp.model.User;
import com.example.MessagingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String username) {
        User user = new User(username);
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
