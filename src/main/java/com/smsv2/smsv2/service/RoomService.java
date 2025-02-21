package com.smsv2.smsv2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.smsv2.smsv2.DTO.RoomDTO;
import com.smsv2.smsv2.entity.Room;

public interface RoomService {
	ResponseEntity<List<Room>> getAllRoom();

	ResponseEntity<Room> getRoomById(int id);

	ResponseEntity<Room> getRoomByRoomId(String roomId);
	ResponseEntity<List<Room>> getRoomByCreatorId(int creatorId);
	ResponseEntity<List<Room>> getRoomByParticipentId(int participentid);

	ResponseEntity<?> createRoom(RoomDTO roomDTO);

	ResponseEntity<?> joinRoom(RoomDTO roomDTO);

	ResponseEntity<?> editRoom(String id, RoomDTO roomDTO);

	String deleteRoom(RoomDTO roomDTO);
	
	int leaveroom(RoomDTO roomDTO);
	int kickroom(RoomDTO roomDTO);
}
