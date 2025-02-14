package com.example.MessagingApp.model;
import jakarta.persistence.*;

@Entity
public class ChannelMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private Role role;

    public ChannelMembership() {
    }

    public ChannelMembership(User user, Channel channel, Role role) {
        this.user = user;
        this.channel = channel;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
