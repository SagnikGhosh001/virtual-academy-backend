package com.smsv2.smsv2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.AssignmentDTO;
import com.smsv2.smsv2.DTO.AssignmentUploadDTO;
import com.smsv2.smsv2.entity.AssignmentUpload;

public interface AssignmentUploadService {
	
	// get all assignment
	ResponseEntity<List<AssignmentUpload>> getAllAssignmentUpload();

	// get assignment by id
	ResponseEntity<Optional<AssignmentUpload>> getAllAssignmentUploadById(int id);

	// get assignment by sub id
	ResponseEntity<List<AssignmentUpload>> getAllAssignmentUploadByAssignmentId(int assignmentId);

	// get assignment by sub id
	ResponseEntity<List<AssignmentUpload>> getAllAssignmentUploadByTeacherId(int teacherId);
	
//	ResponseEntity<String> uploadFile(int id, MultipartFile file);

	byte[] downloadFile(int id);

	// Edit the uploaded PDF by allowing drawing annotations (for teacher)
//	ResponseEntity<byte[]> editUploadedPdf(int uploadId, MultipartFile annotatedPdf);

	
	// add new assignment
	ResponseEntity<?> addAssignmentUpload(AssignmentUploadDTO assignmentUploadDTO,MultipartFile file);

	// update assignment
	ResponseEntity<?> updateAssignmentUpload(int id, AssignmentUploadDTO assignmentUploadDTO,MultipartFile annotatedPdf);

		
		
	// delete a assignment
	ResponseEntity<?> delteAssignmentUploadById(int id,AssignmentUploadDTO assignmentUploadDTO);

	// delete all assignment of same subject
	ResponseEntity<?> deleteAllAssignmentByAssignmentId(int assignmentId,AssignmentUploadDTO assignmentUploadDTO);

	// delete all assignment
	ResponseEntity<?> deleteAllAssignmentUpload(AssignmentUploadDTO assignmentUploadDTO);
	
}
