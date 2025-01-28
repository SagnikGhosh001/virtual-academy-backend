package com.smsv2.smsv2.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.StudentDTO;
import com.smsv2.smsv2.DTO.UserDTO;
import com.smsv2.smsv2.entity.LoginResponse;
import com.smsv2.smsv2.entity.User;

public interface UserService {
	ResponseEntity<LoginResponse> login(UserDTO userDTO);

	ResponseEntity<?>emailVerify(UserDTO userDTO);

	ResponseEntity<?> updateEmail(int id, UserDTO userDTO);

	// update Student
	ResponseEntity<?> addPhone(int id, UserDTO userDTO);

	// phone verify
	ResponseEntity<?> phoneVerify(UserDTO userDTO);

	// send otp to email
	ResponseEntity<?> sendOtpTOEmaail(UserDTO email);

	// update Student password
	ResponseEntity<?> updatePassword(int id, UserDTO userDTO);

	// forget password
	ResponseEntity<?> forgetPassword(UserDTO userDTO);
	
	//isonline true
	ResponseEntity<?> isonlineTrue(int id);
	
	//isonline false
	ResponseEntity<?> isonlineFalse(int id);
	
	//isblocked
	ResponseEntity<?> isblocked(int id,UserDTO userDTO);
	
	byte[] downloadFile(int id);
	
	
	
	ResponseEntity<String> uploadFile(int id, MultipartFile file);
	
	ResponseEntity<String> deleteFile(int id,UserDTO userDTO);


}
