package com.smsv2.smsv2.entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String link;
    
    @JsonFormat(pattern = "yyyy-MM-dd",shape = Shape.STRING)
    private String deadline;
    
    private LocalDate submissionDate;
    
    private LocalDateTime createdAt;
    
	private LocalDateTime modifiedAt;
    
    @Lob
	@Column(name = "pdf", columnDefinition = "LONGBLOB")
	private byte[] pdf;
    
    @Column(length = 2000)
    private String description;
    
    private String subname;
    
    private String deptname;
    
    private String semname;
    
    private String teachername;
    
    @ManyToOne
    @JoinColumn(name = "sub_id", nullable = false)
    @JsonBackReference
    private Sub sub;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonBackReference
    private Teacher teacherId;
    
    @OneToMany(mappedBy = "assignmentId")
	@JsonBackReference
	private List<AssignmentUpload> assignmentUpload= new ArrayList<>();
    
}
