package com.example.MessagingApp.controller;
import com.example.MessagingApp.model.Channel;
import com.example.MessagingApp.model.Message;
import com.example.MessagingApp.model.User;
import com.example.MessagingApp.repository.MessageRepository;
import com.example.MessagingApp.service.ChannelService;
import com.example.MessagingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    // Create a new channel
    // POST /channels?name=General&creatorId=1
    @PostMapping
    public Channel createChannel(@RequestParam String name, @RequestParam Long creatorId) {
        return channelService.createChannel(name, creatorId);
    }

    // Add a user to the channel
    // POST /channels/{channelId}/users?actingUserId=1&newUserId=2
    @PostMapping("/{channelId}/users")
    public String addUserToChannel(@PathVariable Long channelId,
                                   @RequestParam Long actingUserId,
                                   @RequestParam Long newUserId) {
        channelService.addUserToChannel(channelId, actingUserId, newUserId);
        return "User added successfully to the channel";
    }

    // Update the name of the channel
    // PUT /channels/{channelId}?actingUserId=1&newName=NewName
    @PutMapping("/{channelId}")
    public String updateChannelName(@PathVariable Long channelId,
                                    @RequestParam Long actingUserId,
                                    @RequestParam String newName) {
        channelService.updateChannelName(channelId, actingUserId, newName);
        return "The name of the channel has been updated successfully";
    }

    // Delete the channel
    // DELETE /channels/{channelId}?actingUserId=1
    @DeleteMapping("/{channelId}")
    public String deleteChannel(@PathVariable Long channelId, @RequestParam Long actingUserId) {
        channelService.deleteChannel(channelId, actingUserId);
        return "Channel has been deleted successfully";
    }

    // Send a message in the channel
    // POST /channels/{channelId}/messages?senderId=2&content=HelloChannel
    @PostMapping("/{channelId}/messages")
    public Message sendChannelMessage(@PathVariable Long channelId,
                                      @RequestParam Long senderId,
                                      @RequestParam String content) {
        Channel channel = channelService.getChannel(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User sender = userService.getUser(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Message message = new Message(sender, channel, content);
        return messageRepository.save(message);
    }

    // Assigning Admin role to a User in a channel
    // PUT /channels/{channelId}/users/{targetUserId}/role?actingUserId=1
    @PutMapping("/{channelId}/users/{targetUserId}/role")
    public String assignAdminRole(@PathVariable Long channelId,
                                  @PathVariable Long targetUserId,
                                  @RequestParam Long actingUserId) {
        channelService.assignAdminRole(channelId, actingUserId, targetUserId);
        return "User has been promoted to Admin";
    }
}
