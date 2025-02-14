package com.smsv2.smsv2.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
//	public enum MessageType {
//        TEXT, IMAGE, VOICE
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MessageType type;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private Room room;


    private boolean isedited=false;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private LocalDateTime editedtimestamp;

//    private String fileUrl;
//    private String fileSize;
//    private Integer durationSeconds;


}
