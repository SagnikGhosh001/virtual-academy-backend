package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smsv2.smsv2.DTO.MessageDTO;
import com.smsv2.smsv2.DTO.RoomDTO;
import com.smsv2.smsv2.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	@GetMapping("/allroom")
	public ResponseEntity<?> getallmessage() {
		return new ResponseEntity<>(roomService.getAllRoom(), HttpStatus.OK);
	}

	@GetMapping("/roombyid/{id}")
	public ResponseEntity<?> getroomById(@PathVariable("id") int id) {
		return new ResponseEntity<>(roomService.getRoomById(id), HttpStatus.OK);
	}

	@GetMapping("/roombyroomid/{roomid}")
	public ResponseEntity<?> getroomByRoomId(@PathVariable("roomid") String roomid) {
		return new ResponseEntity<>(roomService.getRoomByRoomId(roomid), HttpStatus.OK);
	}
	@GetMapping("/roombycreatorid/{id}")
	public ResponseEntity<?> getroomByCreatorId(@PathVariable("id") int creatorId) {
		return new ResponseEntity<>(roomService.getRoomByCreatorId(creatorId), HttpStatus.OK);
	}
	@GetMapping("/roombyparticipentsid/{id}")
	public ResponseEntity<?> getroomByParticipentId(@PathVariable("id") int participentsid) {
		return new ResponseEntity<>(roomService.getRoomByParticipentId(participentsid), HttpStatus.OK);
	}
	
	@PostMapping("/createroom")
	public ResponseEntity<?> createroom(@RequestBody RoomDTO roomDTO) {

		return new ResponseEntity<>(roomService.createRoom(roomDTO),HttpStatus.CREATED);
	}
	@PostMapping("/joinroom")
	public ResponseEntity<?> joinroom(@RequestBody RoomDTO roomDTO) {
		
		return new ResponseEntity<>(roomService.joinRoom(roomDTO),HttpStatus.OK);
	}
	
	@PutMapping("/editRoom/{id}")
	public ResponseEntity<?> editRoom(@PathVariable("id") String id,@RequestBody RoomDTO roomDTO) {

		return new ResponseEntity<>(roomService.editRoom(id,roomDTO),HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteroombyid/{id}")
	public ResponseEntity<?> deletemessage(@PathVariable("id") String id,@RequestBody RoomDTO roomDTO) {

		return new ResponseEntity<>(roomService.deleteRoom(id,roomDTO),HttpStatus.OK);
	}


}
