package com.smsv2.smsv2.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inbox {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JsonBackReference
	private Teacher teacher;
	
	private String teacherName;
	
	private String teacherDept;
	
	private String studentName; 
	private String studentEmail; 
	private String studentReg; 
	private String studentDept; 
	private String studentSem;
	private boolean isRead=false;
	
	@ManyToOne
	@JsonBackReference
	private Student student;
	private String msg;
	
	private LocalDateTime createdAt;
    
	private LocalDateTime modifiedAt;
}
