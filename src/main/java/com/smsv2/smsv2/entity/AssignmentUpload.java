package com.smsv2.smsv2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.UniqueConstraint;
@Entity
@Table(
	name = "assignmentupload",
	uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"})		
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentUpload {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	 
	 @Lob
	 @Column(name = "pdf", columnDefinition = "LONGBLOB")
	 private byte[] pdf;
	 
	 private LocalDate submitedAt;
	    
	 private LocalDateTime checkedAt;
	 
	 @Min(0)
	 @Max(10)
	 private int marks;
	 
	 private String remarks;
	 
	 @ManyToOne
	 @JsonBackReference
	 private Assignment assignmentId;
	 
	 private String assignmentName;
	 
	 private String subjectname;
	 
	 private String semname;	 
	 
	 @ManyToOne
	 @JsonBackReference
	 private Student studentId;
	 
	 private String studentName;
	 
	 private String deptname;
	 
	 private String reg;
	 
	 @ManyToOne
	 @JsonBackReference
	 private Teacher teacherId;
	 
	 private String teachername;
	 

}
