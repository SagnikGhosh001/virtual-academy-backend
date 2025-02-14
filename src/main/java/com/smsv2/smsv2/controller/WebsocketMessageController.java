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
import com.smsv2.smsv2.entity.Message;
import com.smsv2.smsv2.service.MessageService;

@Controller
public class WebsocketMessageController {

	@Autowired
	private MessageService messageService;

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


}
