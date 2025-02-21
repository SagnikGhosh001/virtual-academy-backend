package com.smsv2.smsv2.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smsv2.smsv2.DTO.RoomDTO;
import com.smsv2.smsv2.dao.RoomDao;
import com.smsv2.smsv2.dao.UserDao;
import com.smsv2.smsv2.entity.Room;
import com.smsv2.smsv2.entity.User;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.exception.WebSocketException;
import com.smsv2.smsv2.service.RoomService;

import jakarta.transaction.Transactional;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDao roomDao;
	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public ResponseEntity<List<Room>> getAllRoom() {
		
		return new ResponseEntity<>(roomDao.findAll(), HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<Room> getRoomById(int id) {
		Room room = roomDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("room", "id", id));
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<Room> getRoomByRoomId(String roomId) {
		Room room = roomDao.findByRoomId(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("room", "roomId", roomId));
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> createRoom(RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getCreatorid())
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", roomDTO.getCreatorid()));
		Optional<Room> room = roomDao.findByRoomId(roomDTO.getRoomId());
		if(room.isPresent()) {
			throw new ResourceBadRequestException("This roomId is already presented use another roomId");
		}
		Room newRoom=new Room();
		newRoom.setName(roomDTO.getName());
		String sanitizedRoomId = roomDTO.getRoomId().trim().replaceAll("\\s+", "");
		newRoom.setRoomId(sanitizedRoomId);
		newRoom.setDescription(roomDTO.getDescription());
		newRoom.setCreator(user);
		newRoom.getParticipants().add(user);
		if(roomDTO.getIsprivate()==1) {
			newRoom.setPrivate(true);
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
			newRoom.setPassword(bcrypt.encode(roomDTO.getPassword()));
		}else {
			newRoom.setPrivate(false);
			newRoom.setPassword(null);
		}
		newRoom.setCreatedDate(LocalDateTime.now());
		newRoom.setLastActivity(LocalDateTime.now());
		roomDao.save(newRoom);
		return new ResponseEntity<>(newRoom,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> editRoom(String roomid, RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getCreatorid())
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", roomDTO.getCreatorid()));
		Room room = roomDao.findByRoomId(roomid)
				.orElseThrow(() -> new ResourceNotFoundException("room", "roomid", roomid));
		if(!room.getCreator().equals(user)) {
			throw new ResourceBadRequestException("You are not allowed");
		}
		room.setName(roomDTO.getName());
		room.setDescription(roomDTO.getDescription());
		room.setLastActivity(LocalDateTime.now());
		if(roomDTO.getIsprivate()==1) {
			room.setPrivate(true);
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
			room.setPassword(bcrypt.encode(roomDTO.getPassword()));
		}else {
			room.setPrivate(false);
			room.setPassword(null);
		}
		roomDao.save(room);
		return new ResponseEntity<>(room,HttpStatus.OK);
	}

	@Override
	@Transactional
	public String deleteRoom(RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getCreatorid())
				.orElseThrow(() -> new WebSocketException("creator not found with id"+ roomDTO.getCreatorid()));
		Room room = roomDao.findByRoomId(roomDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found with id"+ roomDTO.getRoomId()));
		if(!room.getCreator().equals(user)) {
			throw new WebSocketException("You are not allowed");
		}
		room.getParticipants().forEach(p -> p.setJoinedRooms(null));
		room.getParticipants().clear();
		roomDao.deleteById(room.getId());
		String roomId=room.getRoomId();
		return roomId;
	}

	@Override
	@Transactional
	public ResponseEntity<?> joinRoom(RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getJoinid())
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", roomDTO.getJoinid()));
		Room room = roomDao.findByRoomId(roomDTO.getRoomId())
				.orElseThrow(() -> new ResourceNotFoundException("room", "roomid", roomDTO.getRoomId()));
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        if (room.isPrivate() && ! bcrypt.matches(room.getPassword(),roomDTO.getPassword())) {
            throw new ResourceBadRequestException("Incorrect password for private room");
        }
        if(room.getParticipants().contains(user)) {
        	throw new ResourceBadRequestException("You are already in this room");
        }
        room.getParticipants().add(user);
        room.setLastActivity(LocalDateTime.now());
        roomDao.save(room);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Room>> getRoomByCreatorId(int creatorId) {
		List<Room> room=roomDao.findByCreatorId(creatorId);
		return new ResponseEntity<>(room,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Room>> getRoomByParticipentId(int participentid) {
		List<Room> room=roomDao.findByParticipantsId(participentid);
		return new ResponseEntity<>(room,HttpStatus.OK);
	}

	@Override
	@Transactional
	public int leaveroom(RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getJoinid())
				.orElseThrow(() -> new WebSocketException("user not found with id"+roomDTO.getJoinid()));
		Room room = roomDao.findByRoomId(roomDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found with id"+ roomDTO.getRoomId()));
		if(room.getCreator().equals(user)) {
	        throw new WebSocketException("You are not allowed to leave. You are the creator");
	    }
	    

	    if(!room.getParticipants().contains(user)) {
	        throw new WebSocketException("You are not in this room.");
	    }
	    room.getParticipants().remove(user);
	    roomDao.save(room);
		return user.getId();
	}

	@Override
	@Transactional
	public int kickroom(RoomDTO roomDTO) {
		User user =userDao.findById(roomDTO.getJoinid())
				.orElseThrow(() -> new WebSocketException("creator not found with id"+ roomDTO.getJoinid()));
		User creatoruser =userDao.findById(roomDTO.getCreatorid())
				.orElseThrow(() -> new WebSocketException("user not found with id"+ roomDTO.getCreatorid()));
		Room room = roomDao.findByRoomId(roomDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found with id"+ roomDTO.getRoomId()));
		
		if(!room.getCreator().equals(creatoruser)) {
	        throw new WebSocketException("You are not allowed for this action.");
	    }
		if(room.getCreator().equals(user)) {
	        throw new WebSocketException("You are not allowed to kick. You are the creator");
	    }
	    room.getParticipants().remove(user);
	    roomDao.save(room);
		return user.getId();
	}

}
