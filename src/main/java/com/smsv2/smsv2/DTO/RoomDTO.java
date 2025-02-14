package com.smsv2.smsv2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
	private String roomId;
	private String name;
	private String description;
	private int isprivate;
	private String password;
	private int creatorid;
	private int joinid;
}
