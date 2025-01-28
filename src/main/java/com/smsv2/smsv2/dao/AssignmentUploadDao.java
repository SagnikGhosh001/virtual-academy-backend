package com.smsv2.smsv2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.smsv2.smsv2.entity.AssignmentUpload;
import java.util.List;
import java.util.Optional;


public interface AssignmentUploadDao extends JpaRepository<AssignmentUpload, Integer>{
	List<AssignmentUpload> findByAssignmentIdId(int assignmentId);
	
	List<AssignmentUpload> findByTeacherIdId(int teacherId);
	
	@Query("SELECT a FROM AssignmentUpload a WHERE a.assignmentId.id = ?1 AND a.studentId.id = ?2")
	Optional<AssignmentUpload> findByAssignmentIdAndStudentId(int assignmentId,int studentId);
}
