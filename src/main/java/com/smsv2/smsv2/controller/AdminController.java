package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.AdminDTO;
import com.smsv2.smsv2.DTO.StudentDTO;
import com.smsv2.smsv2.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminservice;

	@GetMapping("/allAdmin")
	public ResponseEntity<?> allAdmin() {
		return new ResponseEntity<>(adminservice.getAllAdmin(), HttpStatus.OK);
	}

	@GetMapping("/adminbyId/{id}")
	public ResponseEntity<?> adminbyId(@PathVariable("id") int id) {
		return new ResponseEntity<>(adminservice.getAllAdminById(id), HttpStatus.OK);
	}

	@PostMapping("/registeradmin")
	public ResponseEntity<?> registerStudent(@RequestBody AdminDTO adminDTO) {

		return new ResponseEntity<>(adminservice.addAdmin(adminDTO), HttpStatus.CREATED);

	}

	@PostMapping("/addadmin")
	public ResponseEntity<?> addadmin(@RequestBody AdminDTO adminDTO) {

		return new ResponseEntity<>(adminservice.addAdmin(adminDTO), HttpStatus.CREATED);
	}

	@PutMapping("/updateAdmin/{id}")
	public ResponseEntity<?> updateStudentOthers(@PathVariable("id") int id, @RequestBody AdminDTO adminDTO) {

		return new ResponseEntity<>(adminservice.updateAdmin(id, adminDTO), HttpStatus.OK);
	}

	@DeleteMapping("/deleteAdminbyId/{id}")
	public ResponseEntity<?> deleteStudentbyId(@PathVariable int id, @RequestBody AdminDTO adminDTO) {

		return new ResponseEntity<>(adminservice.delteAdminById(id, adminDTO), HttpStatus.OK);

	}
}
