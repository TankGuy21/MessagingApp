package com.example.MessagingApp.controller;
import com.example.MessagingApp.model.Message;
import com.example.MessagingApp.model.User;
import com.example.MessagingApp.repository.MessageRepository;
import com.example.MessagingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    // Create a new user
    // POST /users?username=Asparuh
    @PostMapping
    public User createUser(@RequestParam String username) {
        return userService.createUser(username);
    }

    // Add a new friend to the user
    // POST /users/{userId}/friends?friendId=2
    @PostMapping("/{userId}/friends")
    public String addFriend(@PathVariable Long userId, @RequestParam Long friendId) {
        User user = userService.getUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userService.getUser(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        user.addFriend(friend);
        userService.saveUser(user);
        return "Friend added successfully";
    }

    // Send a direct message
    // POST /users/{userId}/messages?receiverId=2&content=Hello
    @PostMapping("/{userId}/messages")
    public Message sendDirectMessage(@PathVariable Long userId,
                                     @RequestParam Long receiverId,
                                     @RequestParam String content) {
        User sender = userService.getUser(userId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userService.getUser(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Message message = new Message(sender, receiver, content);
        return messageRepository.save(message);
    }
}
