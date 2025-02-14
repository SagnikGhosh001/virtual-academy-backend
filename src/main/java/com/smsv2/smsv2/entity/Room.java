package com.smsv2.smsv2.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String roomId;  
   

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

//    @Column(nullable = false)
    private boolean isPrivate = false;

    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(
        name = "room_participants",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();
    
    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<Message> message=new ArrayList<>();

    
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

}
