package com.smsv2.smsv2.serviceimpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.UserDTO;
import com.smsv2.smsv2.OtpService.EmailService;
import com.smsv2.smsv2.OtpService.PhoneService;
import com.smsv2.smsv2.config.JwtUtil;
import com.smsv2.smsv2.dao.AdminDao;
import com.smsv2.smsv2.dao.StudentDao;
import com.smsv2.smsv2.dao.TeacherDao;
import com.smsv2.smsv2.dao.UserDao;
import com.smsv2.smsv2.entity.Admin;
import com.smsv2.smsv2.entity.LoginResponse;
import com.smsv2.smsv2.entity.Student;
import com.smsv2.smsv2.entity.Teacher;
import com.smsv2.smsv2.entity.User;
import com.smsv2.smsv2.exception.ResourceBadRequestException;
import com.smsv2.smsv2.exception.ResourceInternalServerErrorException;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userdao;

	@Autowired
	private AdminDao admindao;
	
	@Autowired
	private TeacherDao teacherdao;
	
	@Autowired
	private StudentDao studentdao;
	
	@Autowired
	private EmailService emailservice;

	@Autowired
	private PhoneService phoneservice;

//	@Override
//	public User login(UserDTO userDTO) {
//		User user = userdao.findByEmail(userDTO.getEmail())
//				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));
//		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
//		if (user != null && user.isEmailVerified() && bcrypt.matches(userDTO.getPassword(), user.getPassword())) {
//			return user;
//		} else {
//			throw new ResourceBadRequestException("use valid email and password");
//
//		}
//	}

	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<LoginResponse> login(UserDTO userDTO) {
		User user = userdao.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));

		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		if(user.isIsblocked()) {
			throw new ResourceBadRequestException("You are blocked");
		}
		if(!user.isEmailVerified()) {
			throw new ResourceBadRequestException("Verify Your Account First");
		}
	
		if (user != null  && bcrypt.matches(userDTO.getPassword(), user.getPassword())) {
			// Generate JWT token
			String token = jwtUtil.generateToken(user.getEmail());
			LoginResponse loginResponse = new LoginResponse(token, user);
			// Return user and token
			return new ResponseEntity<>(loginResponse,HttpStatus.OK) ;
		} else {
			throw new ResourceBadRequestException("Hmm, something’s off. Double-check your email or password.");
		}
	}

	@Override
	public ResponseEntity<?> emailVerify(UserDTO userDTO) {
		User user = userdao.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));

		if (user == null) {
			throw new ResourceNotFoundException("user", "email", userDTO.getEmail());
		} else if (user.isEmailVerified()) {
			throw new ResourceBadRequestException("You are already verified");
		} else if (userDTO.getOtp().equals(user.getEmailotp()) && !user.isIsemailOtpUsed()
				&& user.getExpiryDateEmailOtp().isAfter(LocalDateTime.now())) {
			user.setEmailVerified(true);
			user.setIsemailOtpUsed(true);
			userdao.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("invalid or used OTP or expire OTP");

		}

	}

	@Override
	public ResponseEntity<?> updateEmail(int id, UserDTO userDTO) {
		User user = userdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
		Optional<User> emailUser=userdao.findByEmail(userDTO.getEmail());
		Optional<Student> emailStudent=studentdao.findById(id);
		if(emailUser.isPresent()) {
			throw new ResourceInternalServerErrorException("user","email",userDTO.getEmail());
		}
		if (id == userDTO.getCurrentUserId()) {
			user.setEmail(userDTO.getEmail());
			user.getFeedback().forEach(f->f.setEmailId(userDTO.getEmail()));
			if(emailStudent.isPresent()) {
				if(emailStudent.get().getMarks()!=null) {
					emailStudent.get().getMarks().forEach(s->s.setEmail(userDTO.getEmail()));
				}
			}
			user.setEmailVerified(false);
			userdao.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allwed");

		}

	}

	@Override
	public ResponseEntity<?> addPhone(int id, UserDTO userDTO) {
		User user = userdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
		Optional<User> emailUser=userdao.findByPhone(userDTO.getPhone());
		if(emailUser.isPresent()) {
			throw new ResourceInternalServerErrorException("user","phone",userDTO.getPhone());
		}
		if (id == userDTO.getCurrentUserId()) {
			user.setPhone(userDTO.getPhone());
			user.setPhoneverified(false);
			String otp = phoneservice.genereteOtp();
			phoneservice.sendOtp(userDTO.getPhone(), otp);
			user.setExpiryDatePhoneOtp(LocalDateTime.now().plusMinutes(10));
			user.setPhoneotp(otp);
			userdao.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("you are not allwed");

		}

	}

	@Override
	public ResponseEntity<?> phoneVerify(UserDTO userDTO) {
		User user = userdao.findByPhone(userDTO.getPhone())
				.orElseThrow(() -> new ResourceNotFoundException("user", "phone", userDTO.getPhone()));
		if (user == null) {
			throw new ResourceNotFoundException("user", "phone", userDTO.getPhone());

		} else if (user.isPhoneverified()) {
			throw new ResourceBadRequestException("You are already verified");
		} else if (userDTO.getOtp().equals(user.getPhoneotp()) && !user.isPhoneOtpUsed()
				&& user.getExpiryDatePhoneOtp().isAfter(LocalDateTime.now())) {
			user.setPhoneverified(true);
			user.setPhoneOtpUsed(true);
			userdao.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("internal error");

		}

	}

	@Override
	public ResponseEntity<?> sendOtpTOEmaail(UserDTO userDTO) {
		User user = userdao.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));

		String otp = emailservice.genereteOtp();
		user.setEmailotp(otp);
		user.setExpiryDateEmailOtp(LocalDateTime.now().plusMinutes(10));
		emailservice.sendVerficationEmail(userDTO.getEmail(), otp);
		user.setIsemailOtpUsed(false);
		userdao.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updatePassword(int id, UserDTO userDTO) {
		User user = userdao.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));

		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		if (id == userDTO.getCurrentUserId()) {
			if (bcrypt.matches(userDTO.getPassword(), user.getPassword())) {
				user.setPassword(bcrypt.encode(userDTO.getChangePassword()));
				userdao.save(user);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				throw new ResourceBadRequestException("use valid password");

			}
		} else {
			throw new ResourceBadRequestException("you are not allwed");

		}

	}

	@Override
	public ResponseEntity<?> forgetPassword(UserDTO userDTO) {
		User user = userdao.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("user", "email", userDTO.getEmail()));
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		if (userDTO.getOtp().equals(user.getEmailotp()) && !user.isIsemailOtpUsed()
				&& user.getExpiryDateEmailOtp().isAfter(LocalDateTime.now())) {
			user.setPassword(bcrypt.encode(userDTO.getPassword()));
			user.setIsemailOtpUsed(true);
			userdao.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ResourceBadRequestException("invalid otp or expire otp or used Otp");

		}

	}

	@Override
	public ResponseEntity<?> isonlineTrue(int id) {
		User user = userdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
		user.setIsonline(true);
		userdao.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
	@Override
	public ResponseEntity<?> isonlineFalse(int id) {
		User user = userdao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
		user.setIsonline(false);
		userdao.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> isblocked(int id, UserDTO userDTO) {
		System.out.println("Current User ID: " + userDTO.getCurrentUserId());

		Optional<Admin> admin = admindao.findById(userDTO.getCurrentUserId());
	    Optional<Teacher> teacher = teacherdao.findById(userDTO.getCurrentUserId());
	    
	    Student student = studentdao.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("student", "id", id));
	    
	    boolean isAdmin = admin.isPresent() && "admin".equals(admin.get().getRole());
	    boolean isTeacherHOD = teacher.isPresent() && "hod".equals(teacher.get().getRole()) &&
	                           teacher.get().getDept().equals(student.getDept());
	    boolean isTeacherPIC = teacher.isPresent() && "pic".equals(teacher.get().getRole());
	    
	   
	    if (isAdmin || isTeacherHOD || isTeacherPIC) {
	        if (!student.isIsblocked()) {
	            student.setIsblocked(true);
	            if (isAdmin) {
	                student.setBlockedby(admin.get().getEmail());
	            } else if (isTeacherHOD || isTeacherPIC) {
	                student.setBlockedby(teacher.get().getEmail());  // Use teacher's email here
	            }
	        } else {
	            student.setIsblocked(false);
	            student.setBlockedby(null);
	        }
	        studentdao.save(student);
	        return new ResponseEntity<>(HttpStatus.OK);
	    } else {
	        throw new ResourceBadRequestException("Not allowed");
	    }
	}

	
	@Override
	public byte[] downloadFile(int id) {
		User user = userdao.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
		return user.getPic();
		
	}
	
	@Override
	public ResponseEntity<String> uploadFile(int id, MultipartFile file) {
		try {
			User user = userdao.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
			user.setPic(file.getBytes());
			userdao.save(user);
			String msg= "File uploaded successfully";
			return new ResponseEntity<>(msg,HttpStatus.OK);
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload file", e);
		}
	}

	
	@Override
	public ResponseEntity<String> deleteFile(int id,UserDTO userDTO) {
		try {
			String msg;
			
			if(id==userDTO.getCurrentUserId()) {
				
				User user = userdao.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
				user.setPic(null);
				userdao.save(user);
				msg= "File deleted successfully";
				return new ResponseEntity<>(msg,HttpStatus.OK);	
			}else {
				msg= "You are not allowed";
				throw new ResourceBadRequestException(msg);	
			}

			
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete file", e);
		}
	}
}
