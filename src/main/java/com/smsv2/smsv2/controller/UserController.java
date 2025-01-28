package com.smsv2.smsv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.StudentDTO;
import com.smsv2.smsv2.DTO.UserDTO;
import com.smsv2.smsv2.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userservice;

	@PostMapping("/login")
	public ResponseEntity<?> loginStudent(@RequestBody UserDTO userDTO) {

		return new ResponseEntity<>(userservice.login(userDTO), HttpStatus.OK);

	}

	@PostMapping("/verifyEmail")
	public ResponseEntity<?> verifyStudentEmail(@RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(userservice.emailVerify(userDTO),HttpStatus.OK);

	}

	@PutMapping("/updatePhone/{id}")
	public ResponseEntity<?> updatePhoneStudent(@PathVariable int id, @RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.addPhone(id, userDTO),HttpStatus.OK);

	}

	@PostMapping("/verifyPhone")
	public ResponseEntity<?> verifyStudentPhone(@RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.phoneVerify(userDTO),HttpStatus.OK);

	}

	@PutMapping("/updateEmail/{id}")
	public ResponseEntity<?> updateStudentEmail(@PathVariable int id, @RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.updateEmail(id, userDTO),HttpStatus.OK);

	}

	@PutMapping("/updatePassword/{id}")
	public ResponseEntity<?> updateStudentPassword(@PathVariable int id, @RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.updatePassword(id, userDTO),HttpStatus.OK);
	}

	@PutMapping("/blocked/{id}")
	public ResponseEntity<?> isblocked(@PathVariable int id, @RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.isblocked(id, userDTO),HttpStatus.OK);
	}
	
	@PutMapping("/online/{id}")
	public ResponseEntity<?> online(@PathVariable int id) {
		
		return new ResponseEntity<>(userservice.isonlineTrue(id),HttpStatus.OK);
	}
	
	@PutMapping("/offline/{id}")
	public ResponseEntity<?> offline(@PathVariable int id) {
		
		return new ResponseEntity<>(userservice.isonlineFalse(id),HttpStatus.OK);
	}
	
	@PostMapping("/forgetPassword")
	public ResponseEntity<?> forgetStudentPassword(@RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.forgetPassword(userDTO),HttpStatus.OK);
	}

	@PostMapping("/sendOtpEmail")
	public ResponseEntity<?> senOtpStudent(@RequestBody UserDTO userDTO) {
		
		return new ResponseEntity<>(userservice.sendOtpTOEmaail(userDTO),HttpStatus.OK);
	}
	
	@PostMapping("/uploaduserpic/{id}")
	public ResponseEntity<String> uploadFile(@PathVariable("id") int id, @RequestParam("file") MultipartFile file) {
		ResponseEntity<String> responseEntity= userservice.uploadFile(id, file);
		String message = responseEntity.getBody(); 
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}
	
	@PutMapping("/deleteuserpic/{id}")
	public ResponseEntity<String> deleteFile(@PathVariable("id") int id,@RequestBody UserDTO userDTO) {
		ResponseEntity<String> responseEntity= userservice.deleteFile(id,userDTO);
		String message = responseEntity.getBody(); 
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable("id") int id) {
		byte[] fileData = userservice.downloadFile(id);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentDispositionFormData("attachment", "user_image.jpg");

		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
	}
}
