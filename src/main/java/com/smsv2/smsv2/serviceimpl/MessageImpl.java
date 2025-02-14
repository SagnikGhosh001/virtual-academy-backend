package com.smsv2.smsv2.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smsv2.smsv2.DTO.MessageDTO;
import com.smsv2.smsv2.dao.MessageDao;
import com.smsv2.smsv2.dao.RoomDao;
import com.smsv2.smsv2.dao.UserDao;
import com.smsv2.smsv2.entity.Message;
import com.smsv2.smsv2.entity.Room;
import com.smsv2.smsv2.entity.User;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.exception.WebSocketException;
import com.smsv2.smsv2.service.MessageService;

import jakarta.transaction.Transactional;

@Service
public class MessageImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoomDao roomDao;
	
	@Override
	public ResponseEntity<List<Message>> getAllMessage() {
		return new ResponseEntity<>(messageDao.findAll(),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Message> getMessageById(int id) {
		Message message=messageDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("message", "id", id));
		return new ResponseEntity<>(message,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Message>> getMessageByRoomId(String roomid) {
		List<Message> message=messageDao.findByRoomRoomId(roomid);
		return new ResponseEntity<>(message,HttpStatus.OK);
	}

	@Override
	@Transactional
	public Message sendMessage(MessageDTO messageDTO) {
		User user =userDao.findById(messageDTO.getSenderId())
				.orElseThrow(() -> new WebSocketException("user not found"));
		Room room = roomDao.findByRoomId(messageDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found"));
		if(!room.getParticipants().contains(user)){
			throw new WebSocketException("You are not in this room you can't send message here");
		}
		Message message=new Message();
		message.setContent(messageDTO.getContent());
		message.setSender(user);
		message.setRoom(room);
		message.setTimestamp(LocalDateTime.now());
		message.setIsedited(false);
		messageDao.save(message);
		return message;
	}

	@Override
	@Transactional
	public Message editMessage(MessageDTO messageDTO) {
		User user =userDao.findById(messageDTO.getSenderId())
				.orElseThrow(() -> new WebSocketException("user not found"));
		Room room = roomDao.findByRoomId(messageDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found"));
		Message message=messageDao.findById(messageDTO.getId()).orElseThrow(()-> new WebSocketException("message not found"));
		if(!room.getParticipants().contains(user)){
			throw new WebSocketException("You are not in this room you can't edit message here");
		}
		if(!message.getSender().equals(user)) {
			throw new WebSocketException("You are not owner of this message");
		}
		message.setContent(messageDTO.getContent());
		message.setEditedtimestamp(LocalDateTime.now());
		message.setIsedited(true);
		messageDao.save(message);
		return message;
	}

	@Override
	@Transactional
	public int deleteMessage(MessageDTO messageDTO) {
		User user =userDao.findById(messageDTO.getSenderId())
				.orElseThrow(() -> new WebSocketException("user not found"));
		Room room = roomDao.findByRoomId(messageDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found"));
		Message message=messageDao.findById(messageDTO.getId()).orElseThrow(()-> new WebSocketException("message not found"));
		if(!room.getParticipants().contains(user)){
			throw new WebSocketException("You are not in this room you can't delete message here");
		}
		if(!message.getSender().equals(user)) {
			throw new WebSocketException("You are not owner of this message");
		}
		int id=message.getId();
		messageDao.deleteById(message.getId());
		
		return id;
	}

}
