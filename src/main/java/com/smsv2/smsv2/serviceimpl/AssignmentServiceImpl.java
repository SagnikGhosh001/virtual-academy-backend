package com.smsv2.smsv2.serviceimpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.AssignmentDTO;
import com.smsv2.smsv2.dao.AssignmentDao;
import com.smsv2.smsv2.dao.SubDao;
import com.smsv2.smsv2.dao.TeacherDao;
import com.smsv2.smsv2.entity.Assignment;
import com.smsv2.smsv2.entity.Sub;
import com.smsv2.smsv2.entity.Teacher;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.AssignmentService;

@Transactional
@Service
public class AssignmentServiceImpl implements AssignmentService {

	@Autowired
	private AssignmentDao assignmentdao;

	@Autowired
	private SubDao subdao;

	@Autowired
	private TeacherDao teacherdao;

	@Override
	public ResponseEntity<List<Assignment>> getAllAssignment() {
		List<Assignment> assignment= assignmentdao.findAll();
		return new ResponseEntity<>(assignment,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Optional<Assignment>> getAllAssignmentById(int id) {
		Optional<Assignment> assignment = assignmentdao.findById(id);
		if (assignment.isEmpty()) {
			throw new ResourceNotFoundException("assignment", "id", id);
		}
		return new ResponseEntity<>(assignment,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addAssignment(AssignmentDTO assignmentDTO) {

		Sub sub = subdao.findById(assignmentDTO.getSubId())
				.orElseThrow(() -> new ResourceNotFoundException("sub", "id", assignmentDTO.getSubId()));
		Teacher teacher = teacherdao.findById(assignmentDTO.getTeacherId())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", assignmentDTO.getTeacherId()));
		if(sub.getTeacher()==null) {
			throw new ResourceBadRequestException("This subject is not assigned to any teacher");
		}
		if(teacher.getSub()==null) {
			throw new ResourceBadRequestException("You have no subject");
		}
		if (teacher.getSub().contains(sub)) {
			Assignment assignment = new Assignment();
			assignment.setName(assignmentDTO.getName());
			assignment.setLink(assignmentDTO.getLink());
			assignment.setTeacherId(teacher);
			assignment.setSub(sub);
			assignment.setDeptname(sub.getDept().getDeptname());
			assignment.setTeachername(teacher.getName());
			assignment.setSemname(sub.getSem().getSemname());
			assignment.setSubname(sub.getSubname());
			assignment.setDeadline(assignmentDTO.getDeadline());
			assignment.setSubmissionDate(assignmentDTO.getSubmissionDate());
			assignment.setDescription(assignmentDTO.getDescription());
			assignment.setCreatedAt(LocalDateTime.now());
			assignmentdao.save(assignment);
			return new ResponseEntity<>(assignment,HttpStatus.CREATED);
		} else {
			throw new ResourceBadRequestException("you are not allwed for this action");
		}

	}

	@Override
	public ResponseEntity<?> updateAssignment(int id, AssignmentDTO assignmentDTO) {
		Assignment existassignment = assignmentdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("assignment", "id", id));
		Teacher teacher = teacherdao.findById(assignmentDTO.getTeacherId())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", assignmentDTO.getTeacherId()));
		if(existassignment.getTeacherId()==null) {
			throw new ResourceBadRequestException("No teacher is associated");
		}
		if (existassignment.getTeacherId().getId() == assignmentDTO.getTeacherId()) {
			existassignment.setLink(assignmentDTO.getLink());
			existassignment.getAssignmentUpload().forEach(a -> a.setAssignmentName(assignmentDTO.getName()));
			existassignment.setName(assignmentDTO.getName());
			existassignment.getAssignmentUpload().forEach(a->a.setAssignmentName(assignmentDTO.getName()));
			existassignment.setDescription(assignmentDTO.getDescription());
			existassignment.setDeadline(assignmentDTO.getDeadline());
			existassignment.setSubmissionDate(assignmentDTO.getSubmissionDate());
			existassignment.setModifiedAt(LocalDateTime.now());
			assignmentdao.save(existassignment);
			return new ResponseEntity<>(existassignment,HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allwed for this action");

		}

	}

	@Override
	public ResponseEntity<?> delteAssignmentById(int id, AssignmentDTO assignmentDTO) {
		Assignment existassignment = assignmentdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("assignment", "id", id));
		Teacher teacher = teacherdao.findById(assignmentDTO.getTeacherId())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", assignmentDTO.getTeacherId()));
		if(existassignment.getTeacherId()==null) {
			throw new ResourceBadRequestException("No teacher is associated");
		}
		if (existassignment.getTeacherId().getId() == assignmentDTO.getTeacherId()) {
			existassignment.getAssignmentUpload().forEach(assigmentupload -> assigmentupload.setAssignmentId(null));
			existassignment.getAssignmentUpload().clear();
			assignmentdao.delete(existassignment);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allwed for this action");

		}

	}

	@Override
	public ResponseEntity<?> deleteAllAssignment(AssignmentDTO assignmentDTO) {
		Teacher teacher = teacherdao.findById(assignmentDTO.getTeacherId())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", assignmentDTO.getTeacherId()));
		List<Assignment> assignments=assignmentdao.findAll();
		if (teacher.getRole().equals("pic")) {
			for (Assignment assignment : assignments) {
				assignment.getAssignmentUpload().forEach(assigmentupload -> assigmentupload.setAssignmentId(null));
				assignment.getAssignmentUpload().clear();
			}
			assignmentdao.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic ");
		}

	}

	@Override
	public ResponseEntity<List<Assignment>> getAllAssignmentBySubId(int subId) {
		List<Assignment> assignment= assignmentdao.findBySubId(subId);
		return new ResponseEntity<>(assignment,HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> deleteAllAssignmentBySub(int subid,AssignmentDTO assignmentDTO) {
		Teacher teacher = teacherdao.findById(assignmentDTO.getTeacherId())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", assignmentDTO.getTeacherId()));
		Sub sub = subdao.findById(subid)
				.orElseThrow(() -> new ResourceNotFoundException("sub", "id", subid));
		if(teacher.getSub()==null) {
			throw new ResourceBadRequestException("You have no subject");
		}
		if (teacher.getSub().contains(sub)) {
			List<Assignment> assignments = assignmentdao.findBySubId(subid);
			if (!assignments.isEmpty()) {
				for (Assignment assignment : assignments) {
					assignment.getAssignmentUpload().forEach(assigmentupload -> assigmentupload.setAssignmentId(null));
					assignment.getAssignmentUpload().clear();
				}
				assignmentdao.deleteAll(assignments);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				throw new ResourceBadRequestException("no assignment for this subject");

			}
		} else {
			throw new ResourceBadRequestException("you are not allwed");

		}

	}

	@Override
	public ResponseEntity<String> uploadFile(int id, MultipartFile file) {
		try {
			Assignment assignment = assignmentdao.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("assignment", "id", id));
			assignment.setPdf(file.getBytes()); // Assuming 'pdf' field can hold PDF bytes
			assignmentdao.save(assignment);
			String msg= "File uploaded successfully";
			return new ResponseEntity<>(msg,HttpStatus.OK);
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload file", e);
		}
	}

	@Override
	public byte[] downloadFile(int id) {
		Assignment assignment = assignmentdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("assignment", "id", id));
		return assignment.getPdf();
		
	}

}
