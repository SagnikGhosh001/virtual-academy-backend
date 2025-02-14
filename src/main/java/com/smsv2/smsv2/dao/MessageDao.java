package com.smsv2.smsv2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smsv2.smsv2.entity.Message;

public interface MessageDao extends JpaRepository<Message, Integer>{
List<Message> findByRoomRoomId(String room);
}
