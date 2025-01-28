package com.smsv2.smsv2.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smsv2.smsv2.DTO.MarksDTO;
import com.smsv2.smsv2.DTO.TeacherDTO;
import com.smsv2.smsv2.dao.DeptDao;
import com.smsv2.smsv2.dao.MarksDao;
import com.smsv2.smsv2.dao.SemDao;
import com.smsv2.smsv2.dao.StudentDao;
import com.smsv2.smsv2.dao.SubDao;
import com.smsv2.smsv2.dao.TeacherDao;
import com.smsv2.smsv2.entity.Dept;
import com.smsv2.smsv2.entity.Marks;
import com.smsv2.smsv2.entity.Sem;
import com.smsv2.smsv2.entity.Student;
import com.smsv2.smsv2.entity.Sub;
import com.smsv2.smsv2.entity.Teacher;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.MarksService;

@Transactional
@Service
public class MarksServiceImpl implements MarksService {

	@Autowired
	private MarksDao marksDao;

	@Autowired
	private DeptDao deptDao;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private SemDao semDao;

	@Autowired
	private SubDao subDao;

	@Autowired
	private TeacherDao teacherDao;

	@Override
	public ResponseEntity<List<Marks>> getAllMarks() {
		List<Marks> marks= marksDao.findAll();
		return new ResponseEntity<>(marks,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Optional<Marks>> getAllMarksById(int id) {
		Optional<Marks> marksOptional = marksDao.findById(id);
		if (marksOptional.isEmpty()) {
			throw new ResourceNotFoundException("marks", "id", id);
		}
		return new ResponseEntity<>(marksOptional,HttpStatus.OK);
	}
	public ResponseEntity<Optional<Marks>> getAllMarksByStudentId(int id) {
		Optional<Marks> marksOptional = marksDao.findByRegId(id);
		if (marksOptional.isEmpty()) {
			throw new ResourceNotFoundException("marks", "studentid", id);
		}
		return new ResponseEntity<>(marksOptional,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Marks>> getAllMarksByReg(String reg) {
		List<Marks> marks= marksDao.findByReg_Reg(reg);
		return new ResponseEntity<>(marks,HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<List<Marks>> getAllMarksByRegSem(MarksDTO marksDTO) {
		List<Marks> marks=  marksDao.findByReg_RegAndSemname(marksDTO.getReg(),marksDTO.getSemname());
		return new ResponseEntity<>(marks,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addMarks(MarksDTO marksDTO) {
		Student student = studentDao.findById(marksDTO.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("student", "id", marksDTO.getStudentId()));
		Sub sub = subDao.findById(marksDTO.getSubId())
				.orElseThrow(() -> new ResourceNotFoundException("sub", "id", marksDTO.getSubId()));
//		Sem sem = semDao.findById(sub.getSem().getId())
//				.orElseThrow(() -> new ResourceNotFoundException("sem", "id", sub.getSem().getId()));
		Teacher teacher = teacherDao.findById(marksDTO.getTeacher())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", marksDTO.getTeacher()));
//		Optional<Marks> checkMarks=marksDao.findMarksBySubjectAndStudent(sub.getId(), student.getId());
		if (teacher.getDept() == null || teacher.getSem()==null) {
	        throw new ResourceBadRequestException("Teacher's department and sem not found");
	    }
		Optional<Marks> marksList = marksDao.findBySubIdAndRegId(sub.getId(), student.getId());
		if (marksList.isPresent()) {
			throw new ResourceBadRequestException("Marks for the subject '" + sub.getSubname() + "' already exist for this student (" + student.getEmail() + ")");
		}
		if(!student.getDept().equals(sub.getDept()) || !student.getSem().equals(sub.getSem())) {
			
			throw new ResourceBadRequestException("This Subject"+sub.getSubname()	+"is not this "+student.getEmail()+ " Student's Subject");		
		}
		if (teacher.getDept().equals(student.getDept()) && teacher.getSem().contains(student.getSem())) {
			Marks marks = new Marks();
			marks.setReg(student);
			marks.setEmail(student.getEmail());
			marks.setSub(sub);
			marks.setStaticStudentId(student.getId());
			marks.setStaticSubId(sub.getId());
			marks.setMark(marksDTO.getMark());
			marks.setDeptName(sub.getDept().getDeptname());
			marks.setSemname(student.getSemname());
			marks.setRegNo(student.getReg());
			marks.setSubname(sub.getSubname());
			marks.setCreatedAt(LocalDateTime.now());
			marks.setCreateby(teacher.getEmail());
			marksDao.save(marks);
			return new ResponseEntity<>(marks,HttpStatus.CREATED);
		} else {
			throw new ResourceBadRequestException("you are not allowed");
		}

	}

	@Override
	public ResponseEntity<?> updateMarkbyId(int id, MarksDTO marks) {
		Marks existMarks = marksDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("marks", "id", id));
		Teacher teacher = teacherDao.findById(marks.getTeacher())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", marks.getTeacher()));
		Sub sub = subDao.findById(marks.getSubId())
				.orElseThrow(() -> new ResourceNotFoundException("sub", "id", marks.getSubId()));
//		Sem sem = semDao.findById(existMarks.getSub().getSem().getId())
//				.orElseThrow(() -> new ResourceNotFoundException("sem", "id", existMarks.getSub().getSem().getId()));
		if (teacher.getDept() == null || teacher.getSem()==null) {
	        throw new ResourceBadRequestException("Teacher's department and sem not found");
	    }
		if (teacher.getDept().equals(existMarks.getReg().getDept()) && teacher.getSem().contains(existMarks.getReg().getSem())) {
			existMarks.setMark(marks.getMark());
			existMarks.setSub(sub);
			existMarks.setSubname(sub.getSubname());
			existMarks.setDeptName(sub.getDept().getDeptname());
			existMarks.setSemname(sub.getSem().getSemname());
			existMarks.setModifiedAt(LocalDateTime.now());
			existMarks.setModifiedby(teacher.getEmail());
			marksDao.save(existMarks);
			return new ResponseEntity<>(existMarks,HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allowed");
		}

	}

	@Override
	public ResponseEntity<?> delteMarksById(int id, MarksDTO marksDTO) {
		Marks existMarks = marksDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("marks", "id", id));
		Teacher teacher = teacherDao.findById(marksDTO.getTeacher())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", marksDTO.getTeacher()));
		if (teacher.getDept() == null || teacher.getSem()==null) {
	        throw new ResourceBadRequestException("Teacher's department and sem not found");
	    }
		if (teacher.getDept().equals(existMarks.getReg().getDept())
				&& teacher.getSem().contains(existMarks.getSub().getSem())) {
			marksDao.delete(existMarks);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allowed");
		}

	}

	@Override
	public ResponseEntity<?> deleteAllMarks(MarksDTO marksDTO) {
		Teacher teacher = teacherDao.findById(marksDTO.getTeacher())
				.orElseThrow(() -> new ResourceNotFoundException("teacher", "id", marksDTO.getTeacher()));

		if (teacher.getRole().equals("pic")) {
			marksDao.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic");
		}

	}

}
