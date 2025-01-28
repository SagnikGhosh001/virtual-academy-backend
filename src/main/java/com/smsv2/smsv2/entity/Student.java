package com.smsv2.smsv2.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {

	

	private String role = "student";

	@Column(nullable = false)
	private String reg;
	
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonBackReference
	private List<Inbox> inbox = new ArrayList<>();

	@OneToMany(mappedBy = "reg", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonBackReference
	private List<Marks> marks = new ArrayList<>();

	@ManyToOne
	@JsonBackReference
	private Sem sem;
	private int semStaticId;

	private String semname;

	@ManyToOne
	@JsonBackReference
	private Dept dept;
	private int deptStaticId;
	
	@OneToMany(mappedBy = "studentId")
	@JsonBackReference
	private List<AssignmentUpload> assignmentUpload= new ArrayList<>();
    

	private String deptname;
	
	private String modifiedby;
	// self,google,facebook
//	private Providers provider=Providers.SELF;
//	private String providerId;

}
