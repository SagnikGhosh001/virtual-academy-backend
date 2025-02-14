package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smsv2.smsv2.DTO.MessageDTO;
import com.smsv2.smsv2.service.MessageService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/allmessage")
	public ResponseEntity<?> getallmessage() {
		return new ResponseEntity<>(messageService.getAllMessage(), HttpStatus.OK);
	}

	@GetMapping("/messagebyid/{id}")
	public ResponseEntity<?> getmessageById(@PathVariable("id") int id) {
		return new ResponseEntity<>(messageService.getMessageById(id), HttpStatus.OK);
	}

	@GetMapping("/messagebyroomid/{roomid}")
	public ResponseEntity<?> getmessageByRoomId(@PathVariable("roomid") String roomid) {
		return new ResponseEntity<>(messageService.getMessageByRoomId(roomid), HttpStatus.OK);
	}
	
//	@PutMapping("/editmessage/{id}")
//	public ResponseEntity<?> editmessage(@PathVariable("id") int id,@RequestBody MessageDTO messageDTO) {
//
//		return new ResponseEntity<>(messageService.editMessage(messageDTO),HttpStatus.OK);
//	}
	
//	@DeleteMapping("/deletemessagebyid")
//	public ResponseEntity<?> deletemessage(@RequestBody MessageDTO messageDTO) {
//
//		return new ResponseEntity<>(messageService.deleteMessage(messageDTO),HttpStatus.OK);
//	}
}
