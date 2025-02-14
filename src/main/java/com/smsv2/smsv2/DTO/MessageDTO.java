package com.smsv2.smsv2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
	private int id;
	private String roomId;
	private int senderId;
	private String content;
}
