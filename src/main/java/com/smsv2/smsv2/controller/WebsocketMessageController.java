package com.smsv2.smsv2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smsv2.smsv2.DTO.MessageDTO;
import com.smsv2.smsv2.DTO.RoomDTO;
import com.smsv2.smsv2.dao.RoomDao;
import com.smsv2.smsv2.entity.Message;
import com.smsv2.smsv2.entity.Room;
import com.smsv2.smsv2.entity.User;
import com.smsv2.smsv2.exception.WebSocketException;
import com.smsv2.smsv2.service.MessageService;
import com.smsv2.smsv2.service.RoomService;

@Controller
public class WebsocketMessageController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomDao roomDao;
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/room/{roomId}")
	public Message handleChatMessage(@DestinationVariable String roomId,@Payload MessageDTO messageDTO) {
		// Save the message to the database
		Message savedMessage = messageService.sendMessage(messageDTO);

		// Return the saved message to be broadcast to all subscribers
		return savedMessage;
	}
	@MessageMapping("/chat/{roomId}/edit")
    @SendTo("/topic/room/{roomId}")
	public Message handleMessageEdit(
            @DestinationVariable String roomId,
            @Payload MessageDTO editDTO
    ) {
        // Validate and update the message
        Message updatedMessage = messageService.editMessage(editDTO);
        return updatedMessage;
    }
	@MessageMapping("/chat/{roomId}/delete")
	public void handleMessageDelete(
			@DestinationVariable String roomId,
			@Payload MessageDTO editDTO
			) {
		// Validate and delete the message
		int id = messageService.deleteMessage(editDTO);
		// Send structured JSON payload
	    Map<String, Object> payload = new HashMap<>();
	    payload.put("type", "DELETE");
	    payload.put("id", id); // Ensure ID is an integer
	    payload.put("roomId", roomId); // Include roomId for debugging

	    // Broadcast to all clients
	    messagingTemplate.convertAndSend("/topic/room/" + roomId, payload);
	    System.out.println("Broadcasting deletion to /topic/room/" + roomId + ": " + payload);
	}
	@MessageMapping("/room/{roomId}/delete")
	public void handleRoomDelete(
			@DestinationVariable String roomId,
			@Payload RoomDTO editDTO
			) {
		// Validate and delete the message
		String id = roomService.deleteRoom(editDTO);
		// Send structured JSON payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", "ROOMDELETE");
		payload.put("roomId", id); // Ensure ID is an integer
//		payload.put("roomId", roomId); // Include roomId for debugging
		
		// Broadcast to all clients
		messagingTemplate.convertAndSend("/topic/room/" + roomId, payload);
		System.out.println("Broadcasting deletion to /topic/room/" + roomId + ": " + payload);
	}
	@MessageMapping("/room/{roomId}/leave")
	public void handleLeaveRoom(
			@DestinationVariable String roomId,
			@Payload RoomDTO editDTO
			) {
		// Validate and delete the message
		int id = roomService.leaveroom(editDTO);
		// Send structured JSON payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", "LEAVE");
		payload.put("userId", id); // Ensure ID is an integer
//		payload.put("roomId", roomId); // Include roomId for debugging
		
		// Broadcast to all clients
		messagingTemplate.convertAndSend("/topic/room/" + roomId, payload);
		System.out.println("Broadcasting deletion to /topic/room/" + roomId + ": " + payload);
	}
	@MessageMapping("/room/{roomId}/kick")
	public void handleKickRoom(
			@DestinationVariable String roomId,
			@Payload RoomDTO editDTO
			) {
		// Validate and delete the message
		Room room = roomDao.findByRoomId(editDTO.getRoomId())
				.orElseThrow(() -> new WebSocketException("room not found with id"+ editDTO.getRoomId()));
		int id = roomService.kickroom(editDTO);
		// Send structured JSON payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", "KICK");
		payload.put("userId", id); // Ensure ID is an integer
		payload.put("creatorId", room.getCreator().getId()); // Include roomId for debugging
		
		// Broadcast to all clients
		messagingTemplate.convertAndSend("/topic/room/" + roomId, payload);
		System.out.println("Broadcasting deletion to /topic/room/" + roomId + ": " + payload);
	}

	
}
