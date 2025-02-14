package com.example.MessagingApp.service;
import com.example.MessagingApp.model.*;
import com.example.MessagingApp.repository.ChannelMembershipRepository;
import com.example.MessagingApp.repository.ChannelRepository;
import com.example.MessagingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMembershipRepository channelMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new channel, where the user that created it becomes an owner
    public Channel createChannel(String name, Long creatorId) {
        Optional<User> creatorOpt = userRepository.findById(creatorId);
        if (!creatorOpt.isPresent()) {
            throw new RuntimeException("Creator of the channel not found");
        }
        Channel channel = new Channel(name);
        channel = channelRepository.save(channel);

        // Adds the creator as an OWNER
        ChannelMembership membership = new ChannelMembership(creatorOpt.get(), channel, Role.OWNER);
        channelMembershipRepository.save(membership);
        channel.addMembership(membership);
        return channelRepository.save(channel);
    }

    public Optional<Channel> getChannel(Long channelId) {
        return channelRepository.findById(channelId);
    }

    // Add a new user to the channel (OWNER or Admin only)
    public void addUserToChannel(Long channelId, Long actingUserId, Long newUserId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User actingUser = userRepository.findById(actingUserId)
                .orElseThrow(() -> new RuntimeException("Acting user not found"));
        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("New user not found"));

        ChannelMembership actingMembership = channelMembershipRepository.findByChannelAndUser(channel, actingUser)
                .orElseThrow(() -> new RuntimeException("Acting user not a part of the channel"));

        if (actingMembership.getRole() == Role.OWNER || actingMembership.getRole() == Role.ADMIN) {
            if (channelMembershipRepository.findByChannelAndUser(channel, newUser).isPresent()) {
                throw new RuntimeException("User is now part of the channel");
            }
            ChannelMembership membership = new ChannelMembership(newUser, channel, Role.MEMBER);
            channelMembershipRepository.save(membership);
            channel.addMembership(membership);
            channelRepository.save(channel);
        } else {
            throw new RuntimeException("You have no rights to add users");
        }
    }

    // Change the name of the channel (OWNER or ADMIN only)
    public void updateChannelName(Long channelId, Long actingUserId, String newName) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User actingUser = userRepository.findById(actingUserId)
                .orElseThrow(() -> new RuntimeException("Acting user not found"));
        ChannelMembership membership = channelMembershipRepository.findByChannelAndUser(channel, actingUser)
                .orElseThrow(() -> new RuntimeException("User is not part of the channel"));

        if (membership.getRole() == Role.OWNER || membership.getRole() == Role.ADMIN) {
            channel.setName(newName);
            channelRepository.save(channel);
        } else {
            throw new RuntimeException("You have no rights to change the name of the channel");
        }
    }

    // Delete channel (OWNER only)
    public void deleteChannel(Long channelId, Long actingUserId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User actingUser = userRepository.findById(actingUserId)
                .orElseThrow(() -> new RuntimeException("Acting user not found"));
        ChannelMembership membership = channelMembershipRepository.findByChannelAndUser(channel, actingUser)
                .orElseThrow(() -> new RuntimeException("User is not part of the channel"));

        if (membership.getRole() == Role.OWNER) {
            channelRepository.delete(channel);
        } else {
            throw new RuntimeException("Only the owner of the channel can delete it");
        }
    }

    // Assign Admin role (OWNER only)
    public void assignAdminRole(Long channelId, Long actingUserId, Long targetUserId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User actingUser = userRepository.findById(actingUserId)
                .orElseThrow(() -> new RuntimeException("Acting user not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        ChannelMembership actingMembership = channelMembershipRepository.findByChannelAndUser(channel, actingUser)
                .orElseThrow(() -> new RuntimeException("Acting user is not part of the channel"));

        if (actingMembership.getRole() != Role.OWNER) {
            throw new RuntimeException("Only the Owner can assign the Admin role");
        }

        ChannelMembership targetMembership = channelMembershipRepository.findByChannelAndUser(channel, targetUser)
                .orElseThrow(() -> new RuntimeException("Targeted user is not part of the channel"));

        targetMembership.setRole(Role.ADMIN);
        channelMembershipRepository.save(targetMembership);
    }
}
