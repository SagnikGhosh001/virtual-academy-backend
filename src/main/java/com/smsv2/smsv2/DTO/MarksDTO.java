package com.smsv2.smsv2.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksDTO {
	private int mark;
	private int subId;
	private int studentId;
	private String semname;
	private String reg;
	private int teacher;
	
}
