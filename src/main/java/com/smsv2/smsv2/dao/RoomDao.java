package com.smsv2.smsv2.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smsv2.smsv2.entity.Room;

public interface RoomDao extends JpaRepository<Room, Integer>{
	Optional<Room> findByRoomId(String roomId);
	List<Room> findByCreatorId(int creatorId);
	List<Room> findByParticipantsId(int participantsid);
}
