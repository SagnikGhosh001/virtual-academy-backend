package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.StudentDTO;
import com.smsv2.smsv2.entity.Student;
import com.smsv2.smsv2.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	private StudentService studentservice;

	@GetMapping("/allStudent")
	public ResponseEntity<?> allStudent() {
		return new ResponseEntity<>(studentservice.getAllStudent(), HttpStatus.OK);
	}

	@GetMapping("/studentbyId/{id}")
	public ResponseEntity<?> studentbyId(@PathVariable("id") int id) {
		return new ResponseEntity<>(studentservice.getAllStudentById(id), HttpStatus.OK);
	}

	@GetMapping("/studentbyDeptId/{id}")
	public ResponseEntity<?> studentbyDeptId(@PathVariable("id") int id) {
		return new ResponseEntity<>(studentservice.getAllStudentByDept(id), HttpStatus.OK);
	}

	@GetMapping("/studentbySemId/{id}")
	public ResponseEntity<?> studentbySemId(@PathVariable("id") int id) {
		return new ResponseEntity<>(studentservice.getAllStudentBySem(id), HttpStatus.OK);
	}

	@GetMapping("/studentbyEmail/{email}")
	public ResponseEntity<?> studentbyEmail(@PathVariable("email") String email) {
		return new ResponseEntity<>(studentservice.getAllStudentByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/studentbyPhone/{phone}")
	public ResponseEntity<?> studentbyPhone(@PathVariable("phone") String phone) {
		return new ResponseEntity<>(studentservice.getAllStudentByPhone(phone), HttpStatus.OK);
	}

	@GetMapping("/studentbyReg/{reg}")
	public ResponseEntity<?> studentbyReg(@PathVariable("reg") String reg) {
		return new ResponseEntity<>(studentservice.getAllStudentByReg(reg), HttpStatus.OK);
	}

	@GetMapping("/studentbySemDept/{sem}/{dept}")
	public ResponseEntity<?> studentbySemDept(@PathVariable("sem") int sem, @PathVariable("dept") int dept) {
		return new ResponseEntity<>(studentservice.getAllStudentBySemandDept(sem, dept), HttpStatus.OK);
	}

	@PostMapping("/registerstudent")
	public ResponseEntity<?> registerStudent(@RequestBody StudentDTO studentDTO) {
		return new ResponseEntity<>(studentservice.addStudent(studentDTO),HttpStatus.CREATED);

	}

	

	

	@PutMapping("/updateStudentDetails/{id}")
	public ResponseEntity<?> updateStudentDetails(@PathVariable("id") int id, @RequestBody StudentDTO studentDTO) {
		
		return new ResponseEntity<>(studentservice.updateStudent(id, studentDTO),HttpStatus.OK);
	}

	@PutMapping("/updateStudentOthers/{id}")
	public ResponseEntity<?> updateStudentOthers(@PathVariable("id") int id, @RequestBody StudentDTO studentDTO) {
		
		return new ResponseEntity<>(studentservice.updateStudentOthers(id, studentDTO),HttpStatus.OK);

	}

	@DeleteMapping("/deleteStudentbyId/{id}")
	public ResponseEntity<?> deleteStudentbyId(@PathVariable("id") int id, @RequestBody StudentDTO studentDTO) {
		
		return new ResponseEntity<>(studentservice.delteStudentById(id, studentDTO),HttpStatus.OK);

	}

	@DeleteMapping("/deleteAllStudent")
	public ResponseEntity<?> deleteAllStudent(@RequestBody StudentDTO studentDTO) {

		
		return new ResponseEntity<>(studentservice.deleteAllStudent(studentDTO),HttpStatus.OK);

	}
}
