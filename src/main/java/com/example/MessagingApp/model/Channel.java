package com.example.MessagingApp.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembership> memberships = new ArrayList<>();

    public Channel() {
    }

    public Channel(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ChannelMembership> getMemberships() {
        return memberships;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMembership(ChannelMembership membership) {
        memberships.add(membership);
    }
}
