package com.smsv2.smsv2.serviceimpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.smsv2.smsv2.DTO.AssignmentDTO;
import com.smsv2.smsv2.DTO.AssignmentUploadDTO;
import com.smsv2.smsv2.dao.AssignmentDao;
import com.smsv2.smsv2.dao.AssignmentUploadDao;
import com.smsv2.smsv2.dao.StudentDao;
import com.smsv2.smsv2.dao.TeacherDao;
import com.smsv2.smsv2.entity.Assignment;
import com.smsv2.smsv2.entity.AssignmentUpload;
import com.smsv2.smsv2.entity.Student;
import com.smsv2.smsv2.entity.Teacher;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.AssignmentUploadService;

@Service
public class AssignmentUploadServiceImpl implements AssignmentUploadService {

	@Autowired
	private AssignmentUploadDao assignmentUploadDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private AssignmentDao assignmentDao;
	
	@Override
	public ResponseEntity<List<AssignmentUpload>> getAllAssignmentUpload() {
		List<AssignmentUpload> assignmentupload=assignmentUploadDao.findAll();
		return new ResponseEntity<>(assignmentupload,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Optional<AssignmentUpload>> getAllAssignmentUploadById(int id) {
		Optional<AssignmentUpload> assignmentUpload = assignmentUploadDao.findById(id);
		if (assignmentUpload.isEmpty()) {
			throw new ResourceNotFoundException("assignmentUpload", "id", id);
		}
		return new ResponseEntity<>(assignmentUpload,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AssignmentUpload>> getAllAssignmentUploadByAssignmentId(int assignmentId) {
		List<AssignmentUpload> assignmentUpload=assignmentUploadDao.findByAssignmentIdId(assignmentId);
		return new ResponseEntity<>(assignmentUpload,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AssignmentUpload>> getAllAssignmentUploadByTeacherId(int teacherId) {
		List<AssignmentUpload> assignmentUpload=assignmentUploadDao.findByTeacherIdId(teacherId);
		return new ResponseEntity<>(assignmentUpload,HttpStatus.OK);
	}
	
//	@Override
//	public ResponseEntity<String> uploadFile(int id, MultipartFile file) {
//		try {
//			AssignmentUpload assignmentUpload = assignmentUploadDao.findById(id)
//					.orElseThrow(() -> new ResourceNotFoundException("assignmentUpload", "id", id));
//			assignmentUpload.setPdf(file.getBytes());
//			assignmentUploadDao.save(assignmentUpload);
//			String msg= "File uploaded successfully";
//			return new ResponseEntity<>(msg,HttpStatus.OK);
//		} catch (IOException e) {
//			throw new RuntimeException("Failed to upload file", e);
//		}
//	}

	@Override
	public byte[] downloadFile(int id) {
		AssignmentUpload assignmentUpload = assignmentUploadDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("assignmentUpload", "id", id));
		return assignmentUpload.getPdf();
		
	}



	
	@Override
	public ResponseEntity<?> addAssignmentUpload(AssignmentUploadDTO assignmentUploadDTO,MultipartFile file) {
		Student student=studentDao.findById(assignmentUploadDTO.getStudentId()).orElseThrow(()->
			new ResourceNotFoundException("student","Id",assignmentUploadDTO.getStudentId()));
		Assignment assignment=assignmentDao.findById(assignmentUploadDTO.getAssignmentId()).orElseThrow(()->
			new ResourceNotFoundException("assignment","Id",assignmentUploadDTO.getAssignmentId()));
		Optional<AssignmentUpload> checkAssignmentUpload=
				assignmentUploadDao.findByAssignmentIdAndStudentId(assignmentUploadDTO.getAssignmentId(), assignmentUploadDTO.getStudentId());
		if(checkAssignmentUpload.isPresent()) {
			throw new ResourceBadRequestException("you already submitted the assignment, Registration no: "+student.getReg());
		}
		if(assignment.getTeacherId()==null) {
			throw new ResourceBadRequestException("This assignment is not assigned to any teacher");
		}
		if(LocalDate.now().isAfter(assignment.getSubmissionDate())) {
			throw new ResourceBadRequestException("TimesUp for submission");
		}
		if(student.getSem().equals(assignment.getSub().getSem())
				&& student.getDeptname().equals(assignment.getDeptname())) {
			AssignmentUpload assignmentUpload=new AssignmentUpload();
			
			assignmentUpload.setAssignmentId(assignment);
			assignmentUpload.setStudentId(student);
			assignmentUpload.setSubmitedAt(LocalDate.now());
			assignmentUpload.setAssignmentName(assignment.getName());
			assignmentUpload.setSubjectname(assignment.getSubname());
			assignmentUpload.setSemname(assignment.getSemname());
			assignmentUpload.setStudentName(student.getName());
			assignmentUpload.setDeptname(assignment.getDeptname());
			assignmentUpload.setReg(student.getReg());
			assignmentUpload.setTeacherId(assignment.getTeacherId());
			assignmentUpload.setTeachername(assignment.getTeacherId().getName());
			try {
				assignmentUpload.setPdf(file.getBytes());
			} catch (IOException e) {
				throw new RuntimeException("Failed to upload file", e);
			}
			assignmentUploadDao.save(assignmentUpload);
			return new ResponseEntity<>(assignmentUpload,HttpStatus.CREATED);
		}else {
			throw new ResourceBadRequestException("you are not allowed");
		}
		
	}



	@Override
	public ResponseEntity<?> updateAssignmentUpload(int id, AssignmentUploadDTO assignmentUploadDTO, MultipartFile annotatedPdf) {
	    AssignmentUpload assignmentUpload = assignmentUploadDao.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("AssignmentUpload", "id", id));
	    
	    Teacher teacher = teacherDao.findById(assignmentUploadDTO.getTeacherId())
	            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", assignmentUploadDTO.getTeacherId()));
	    

	    if (teacher.getId() == assignmentUpload.getTeacherId().getId()) {
	        // Updating fields
	        assignmentUpload.setCheckedAt(LocalDateTime.now());
	        assignmentUpload.setMarks(assignmentUploadDTO.getMarks());
	        assignmentUpload.setRemarks(assignmentUploadDTO.getRemarks());
	        
	        // Updating the PDF if provided
	        if (annotatedPdf != null && !annotatedPdf.isEmpty()) {
	            try {
	                byte[] newPdfContent = annotatedPdf.getBytes();
	                assignmentUpload.setPdf(newPdfContent);
	            } catch (IOException e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                     .body("Failed to update PDF file");
	            }
	        }
	        
	        assignmentUploadDao.save(assignmentUpload);
	        return new ResponseEntity<>(assignmentUpload,HttpStatus.OK);
	    }
	    
	    return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                         .body("Teacher is not authorized to update this assignment");
	}

	
	@Override
	public ResponseEntity<?> delteAssignmentUploadById(int id, AssignmentUploadDTO assignmentUploadDTO) {
		AssignmentUpload assignmentUpload = assignmentUploadDao.findById(id).
				orElseThrow(()-> new ResourceNotFoundException("assigmentupload", "id", id));
		Teacher teacher= teacherDao.findById(assignmentUploadDTO.getTeacherId()).
				orElseThrow(()-> new ResourceNotFoundException("Teacher", "id", assignmentUploadDTO.getTeacherId()));
		if(assignmentUpload.getTeacherId()==null) {
			throw new ResourceBadRequestException("no teacher is associated with this assignment upload");
		}
		if(assignmentUpload.getTeacherId().equals(teacher)) {
			assignmentUploadDao.delete(assignmentUpload);
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			throw new ResourceBadRequestException("you are not allowed for this action");
		}
	}

	@Override
	public ResponseEntity<?> deleteAllAssignmentByAssignmentId(int assignmentId, AssignmentUploadDTO assignmentUploadDTO) {
		Assignment assignment = assignmentDao.findById(assignmentId).
				orElseThrow(()-> new ResourceNotFoundException("assigment", "id", assignmentId));
		Teacher teacher= teacherDao.findById(assignmentUploadDTO.getTeacherId()).
				orElseThrow(()-> new ResourceNotFoundException("Teacher", "id", assignmentUploadDTO.getTeacherId()));
		if(assignment.getTeacherId()==null) {
			throw new ResourceBadRequestException("no teacher is associated with this assignment");
		}
		if(assignment.getTeacherId().equals(teacher)) {
			List<AssignmentUpload> assignmentUpload=assignmentUploadDao.findByAssignmentIdId(assignmentId);
			assignmentUploadDao.deleteAll(assignmentUpload);
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			throw new ResourceBadRequestException("you are not allowed for this action");
		}
	}

	@Override
	public ResponseEntity<?> deleteAllAssignmentUpload(AssignmentUploadDTO assignmentUploadDTO) {
		Teacher teacher= teacherDao.findById(assignmentUploadDTO.getTeacherId()).
				orElseThrow(()-> new ResourceNotFoundException("Teacher", "id", assignmentUploadDTO.getTeacherId()));
		if(teacher.getRole().equals("pic")) {
			assignmentUploadDao.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			throw new ResourceBadRequestException("you are not allowed for this action");
		}
		
	}

}
