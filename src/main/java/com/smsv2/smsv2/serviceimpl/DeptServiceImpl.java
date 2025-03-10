package com.smsv2.smsv2.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smsv2.smsv2.DTO.DeptDTO;
import com.smsv2.smsv2.dao.AdminDao;
import com.smsv2.smsv2.dao.BookDao;
import com.smsv2.smsv2.dao.DeptDao;
import com.smsv2.smsv2.dao.MarksDao;
import com.smsv2.smsv2.dao.SemDao;
import com.smsv2.smsv2.dao.StudentDao;
import com.smsv2.smsv2.dao.TeacherDao;
import com.smsv2.smsv2.entity.Admin;
import com.smsv2.smsv2.entity.Book;
import com.smsv2.smsv2.entity.Dept;
import com.smsv2.smsv2.entity.Marks;
import com.smsv2.smsv2.entity.Sem;
import com.smsv2.smsv2.entity.Student;
import com.smsv2.smsv2.entity.Teacher;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceInternalServerErrorException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.DeptService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptDao deptDao;

	@Autowired
	private SemDao semDao;

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private AdminDao adminDao;

	@Override
	public ResponseEntity<List<Dept>> getAllDept() {
		List<Dept>dept= deptDao.findAll();
		return new ResponseEntity<>(dept,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Optional<Dept>> getAllDeptById(int id) {
		Optional<Dept> dept = deptDao.findById(id);
		if (dept.isEmpty()) {
			throw new ResourceNotFoundException("dept", "id", id);
		}
		return new ResponseEntity<>(dept,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Dept>> getAllDeptByTeacherId(int teacherId) {
		List<Dept> dept= deptDao.findByTeacherId(teacherId);
		return new ResponseEntity<>(dept,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Dept>> getAllDeptBySemId(int semId) {
		List<Dept> dept= deptDao.findBySemId(semId);
		return new ResponseEntity<>(dept,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addDept(DeptDTO deptDTO) {
		
		Sem sem = semDao.findById(deptDTO.getSemId())
				.orElseThrow(() -> new ResourceNotFoundException("sem", "id", deptDTO.getSemId()));
		Dept dept = deptDao.findByDeptname(deptDTO.getDeptname());
		Optional<Teacher> teacher = teacherDao.findById(deptDTO.getUserid());
		Optional<Admin> admin = adminDao.findById(deptDTO.getUserid());
		if ((teacher.isPresent() && teacher.get().getRole().equals("pic"))
				|| (admin.isPresent() && admin.get().getRole().equals("admin"))) {
			if (dept == null) {
				dept = new Dept();
				dept.setDeptname(deptDTO.getDeptname());
				dept.getSem().add(sem);
				dept.setSemStaticId(sem.getId());
			} else {
				if (!dept.getSem().contains(sem)) {
					dept.getSem().add(sem);
					dept.setSemStaticId(sem.getId());
				} else {
					throw new ResourceBadRequestException("Already added");
				}

			}
			dept.setCreatedAt(LocalDateTime.now());
			deptDao.save(dept);
			return new ResponseEntity<>(dept,HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be  pic or admin");
		}

	}

	@Override
	public ResponseEntity<?> updateDept(int id, DeptDTO deptDTO) {
		Dept dept = deptDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("dept", "id", id));
		Sem sem = semDao.findById(deptDTO.getSemId())
				.orElseThrow(() -> new ResourceNotFoundException("sem", "id", deptDTO.getSemId()));
		Optional<Teacher> teacher = teacherDao.findById(deptDTO.getUserid());
		Optional<Admin> admin = adminDao.findById(deptDTO.getUserid());
		Dept nameDept=deptDao.findByDeptname(deptDTO.getDeptname());
		if(nameDept!=null && nameDept.getId() != id) {
			throw new ResourceInternalServerErrorException("dept","name",nameDept.getDeptname());
		}
		if ((teacher.isPresent() && teacher.get().getRole().equals("pic"))
				|| (admin.isPresent() && admin.get().getRole().equals("admin"))) {
			dept.setDeptname(deptDTO.getDeptname());
			dept.getSub().forEach(d->d.getAssignment().forEach(a->a.setDeptname(deptDTO.getDeptname())));
			dept.getSub().forEach(d->d.getAttendance().forEach(a->a.setDeptname(deptDTO.getDeptname())));
			dept.getSub().forEach(d->d.getBook().forEach(a->a.setDeptname(deptDTO.getDeptname())));
			dept.getSub().forEach(d->d.getMarks().forEach(a->a.setDeptName(deptDTO.getDeptname())));
			dept.getSub().forEach(d->d.getNotes().forEach(a->a.setDeptname(deptDTO.getDeptname())));
			dept.getSub().forEach(d->d.getTopics().forEach(a->a.setDeptname(deptDTO.getDeptname())));
			dept.getStudent().forEach(d->d.setDeptname(dept.getDeptname()));
			dept.getSub().forEach(d->d.setDeptname(dept.getDeptname()));
			dept.getSub().forEach(d->d.getAssignment().forEach(a->a.getAssignmentUpload().forEach(au->au.setDeptname(deptDTO.getDeptname()))));
			if (!dept.getSem().contains(sem)) {
				dept.getSem().add(sem);
			}
			dept.setSemStaticId(sem.getId());
			deptDao.save(dept);
			return new ResponseEntity<>(dept,HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic or admin");
		}

	}

	@Override
	public ResponseEntity<?> delteDeptById(int id, DeptDTO deptDTO) {
		Dept dept = deptDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("dept", "id", id));
		Optional<Teacher> checkteacher = teacherDao.findById(deptDTO.getUserid());
		Optional<Admin> admin = adminDao.findById(deptDTO.getUserid());
		if ((checkteacher.isPresent() && checkteacher.get().getRole().equals("pic"))
				|| (admin.isPresent() && admin.get().getRole().equals("admin"))) {
			// Clear the association between Dept and Sem
			dept.getSem().forEach(sem -> sem.getDept().remove(dept));
			dept.getSem().clear();

			// Clear the association between Dept and Teacher
			dept.getTeacher().forEach(teacher -> teacher.setDept(null));
			dept.getTeacher().forEach(teacher -> teacher.setDeptname(null));
			dept.getTeacher().clear();

			// Clear the association between Dept and Student
			dept.getStudent().forEach(student -> student.setDept(null));
			dept.getStudent().forEach(student -> student.setDeptname(null));
			dept.getStudent().clear();

			// Delete the department only once
			deptDao.delete(dept);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic or admin");
		}

	}

	@Override
	public ResponseEntity<?> delteDeptSemById(int id, DeptDTO deptDTO) {
		// Fetch the Dept entity from the database
		Dept dept = deptDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("dept", "id", id));

		// Fetch the Sem entity from the database
		Sem sem = semDao.findById(deptDTO.getSemId())
				.orElseThrow(() -> new ResourceNotFoundException("sem", "id", deptDTO.getSemId()));
		Optional<Teacher> checkteacher = teacherDao.findById(deptDTO.getUserid());
		Optional<Admin> admin = adminDao.findById(deptDTO.getUserid());
		if ((checkteacher.isPresent() && checkteacher.get().getRole().equals("pic"))
				|| (admin.isPresent() && admin.get().getRole().equals("admin"))) {
			// Remove the specific Sem from the Dept's sem list
			if (dept.getSem().contains(sem)) {
				dept.getSem().remove(sem);
				// If Sem has a list of Depts, remove the association from Sem as well
				sem.getDept().remove(dept);
			} else {
				throw new ResourceBadRequestException(
						"Sem with id " + deptDTO.getSemId() + " is not associated with Dept with id " + id);
			}

			// Save the updated Dept entity
			deptDao.save(dept);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic or admin");
		}

	}

	@Override
	public ResponseEntity<?> deleteAllDept(DeptDTO deptDTO) {
		// Fetch all departments from the database
		List<Dept> depts = deptDao.findAll();

		Optional<Teacher> checkteacher = teacherDao.findById(deptDTO.getUserid());
		Optional<Admin> admin = adminDao.findById(deptDTO.getUserid());
		if ((checkteacher.isPresent() && checkteacher.get().getRole().equals("pic"))
				|| (admin.isPresent() && admin.get().getRole().equals("admin"))) {
			// Iterate over each department and perform necessary dissociations
			for (Dept dept : depts) {
				// Clear the association between Dept and Sem
				dept.getSem().forEach(sem -> sem.getDept().remove(dept));
				dept.getSem().clear();

				// Clear the association between Dept and Teacher
				dept.getTeacher().forEach(teacher -> teacher.setDept(null));
				dept.getTeacher().forEach(teacher -> teacher.setDeptname(null));
				dept.getTeacher().clear();

				// Clear the association between Dept and Student
				dept.getStudent().forEach(student -> student.setDept(null));
				dept.getStudent().forEach(student -> student.setDeptname(null));
				dept.getStudent().clear();
			}

			// Delete all departments from the database
			deptDao.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("your role should be pic or admin");
		}

	}

}
