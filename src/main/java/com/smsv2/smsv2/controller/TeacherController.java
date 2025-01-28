package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.DeptDTO;
import com.smsv2.smsv2.DTO.SemDTO;
import com.smsv2.smsv2.DTO.TeacherDTO;
import com.smsv2.smsv2.service.StudentService;
import com.smsv2.smsv2.service.TeacherService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

	@Autowired
	private TeacherService teacherservice;

	@GetMapping("/allteacher")
	public ResponseEntity<?> getAllTeacher() {
		return new ResponseEntity<>(teacherservice.getAllTeacher(), HttpStatus.OK);
	}

	@GetMapping("/teacherById/{id}")
	public ResponseEntity<?> teacherById(@PathVariable("id") int id) {
		return new ResponseEntity<>(teacherservice.getAllTeacherById(id), HttpStatus.OK);
	}

	@GetMapping("/teacherBysemId/{id}")
	public ResponseEntity<?> teacherBySemId(@PathVariable("id") int id) {
		return new ResponseEntity<>(teacherservice.getAllTeacherBySemId(id), HttpStatus.OK);
	}

	@GetMapping("/teacherBydeptId/{id}")
	public ResponseEntity<?> teacherByDeptId(@PathVariable("id") int id) {
		return new ResponseEntity<>(teacherservice.getAllTeacherByDeptId(id), HttpStatus.OK);
	}

	@GetMapping("/teacherBysemdeptId/{semid}/{deptid}")
	public ResponseEntity<?> teacherBysemDeptId(@PathVariable int semid, @PathVariable int deptid) {
		return new ResponseEntity<>(teacherservice.getAllTeacherBySemDeptId(semid, deptid), HttpStatus.OK);
	}

	@GetMapping("/teacherByEmail/{email}")
	public ResponseEntity<?> teacherByEmail(@PathVariable String email) {
		return new ResponseEntity<>(teacherservice.getAllTeacherByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/teacherByPhone/{phone}")
	public ResponseEntity<?> teacherByphone(@PathVariable String phone) {
		return new ResponseEntity<>(teacherservice.getAllTeacherByPhone(phone), HttpStatus.OK);
	}

	
	

	@PostMapping("/registerTeacher")
	public ResponseEntity<?> registerTeacher(@RequestBody TeacherDTO teacherDTO) {
		
		return new ResponseEntity<>(teacherservice.addTeacher(teacherDTO),HttpStatus.CREATED);

	}

	@PutMapping("/updateteacherdetails/{id}")
	public ResponseEntity<?> updateteacherdetails(@PathVariable int id, @RequestBody TeacherDTO teacherDTO) {
		
		return new ResponseEntity<>(teacherservice.updateTeacher(id, teacherDTO),HttpStatus.OK);
	}

	@PutMapping("/updateteacherOthers/{id}")
	public ResponseEntity<?> updateteacherOthers(@PathVariable int id, @RequestBody TeacherDTO teacherDTO) {
		
		return new ResponseEntity<>(teacherservice.updateTeacherOthers(id, teacherDTO),HttpStatus.OK);
	}

	@DeleteMapping("/deleteteacherbyId/{id}")
	public ResponseEntity<?> deleteteacherbyId(@PathVariable int id,@RequestBody TeacherDTO teacherDTO) {
			
			return new ResponseEntity<>(teacherservice.delteTeacherById(id,teacherDTO),HttpStatus.OK);
		
	}

	@DeleteMapping("/deleteParticularSemOfTeacherbyId/{id}")
	public ResponseEntity<?> deleteParticularSemOfTeacherbyId(@PathVariable int id,
			@RequestBody TeacherDTO teacherDTO) {
			
			return new ResponseEntity<>(teacherservice.delteTeacherSemById(id, teacherDTO),HttpStatus.OK);
		
	}

	@DeleteMapping("/deleteParticularDeptOfTeacherbyId/{id}")
	public ResponseEntity<?> deleteParticularDeptOfTeacherbyId(@PathVariable int id,
			@RequestBody TeacherDTO teacherDTO) {
			
			return new ResponseEntity<>(teacherservice.delteTeacherDeptById(id, teacherDTO),HttpStatus.OK);

	}

	@DeleteMapping("/deleteAllteacher")
	public ResponseEntity<?> deleteAllteacher(@RequestBody TeacherDTO teacherDTO) {
			
			return new ResponseEntity<>(teacherservice.deleteAllTeacher(teacherDTO),HttpStatus.OK);
		
	}
}
