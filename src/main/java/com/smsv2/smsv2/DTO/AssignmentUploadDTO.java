package com.smsv2.smsv2.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentUploadDTO {
	private int assignmentId;
	private int studentId;
	@Min(0)
	@Max(10)
	private int marks;
	private String remarks;
	private int teacherId;
}
