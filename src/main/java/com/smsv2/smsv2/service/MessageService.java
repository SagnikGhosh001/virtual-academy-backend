package com.smsv2.smsv2.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.smsv2.smsv2.DTO.MessageDTO;
import com.smsv2.smsv2.entity.Message;


public interface MessageService {
	ResponseEntity<List<Message>> getAllMessage();

	ResponseEntity<Message> getMessageById(int id);

	ResponseEntity<List<Message>> getMessageByRoomId(String roomid);

	Message sendMessage(MessageDTO messageDTO);

	Message editMessage(MessageDTO messageDTO);

	int deleteMessage(MessageDTO messageDTO);
}
