package com.smsv2.smsv2.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsv2.smsv2.DTO.AttendenceDTO;
import com.smsv2.smsv2.entity.Assignment;
import com.smsv2.smsv2.entity.Attendence;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.AttendenceService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/attendence")
public class AttendenceController {
	@Autowired
	private AttendenceService attendenceService;

	@GetMapping("/getallattendence")
	public ResponseEntity<?> getAllAssignment() {
		return new ResponseEntity<>(attendenceService.getAllAttendence(), HttpStatus.OK);
	}

	@GetMapping("/getattendencebyid/{id}")
	public ResponseEntity<?> getAssignmentById(@PathVariable("id") int id) {
		return new ResponseEntity<>(attendenceService.getAllAttendenceById(id), HttpStatus.OK);
	}

	@GetMapping("/getattendencebysubid/{subid}")
	public ResponseEntity<?> getAssignmentBySubId(@PathVariable("subid") int subId) {
		return new ResponseEntity<>(attendenceService.getAllAttendenceBySubId(subId), HttpStatus.OK);
	}

	@PostMapping("/addattendence")
	public ResponseEntity<?> addAttendence(@RequestBody AttendenceDTO attendenceDTO) {

			
			return new ResponseEntity<>(attendenceService.addAssignment(attendenceDTO),HttpStatus.CREATED);

		
	}

	@PutMapping("/updateattendence/{id}")
	public ResponseEntity<?> updateAttendence(@PathVariable("id") int id, @RequestBody AttendenceDTO attendenceDTO) {
		
			return new ResponseEntity<>(attendenceService.updateAttendence(id, attendenceDTO),HttpStatus.OK);

		
	}

	@PostMapping("/uploadpdf/{id}/{role}")
	public ResponseEntity<String> uploadPdf(@PathVariable("role") String role, @PathVariable("id") int id,
			@RequestParam("file") MultipartFile file) {
		if (role.equals("teacher") || role.equals("hod") || role.equals("pic")) {

			ResponseEntity<String> responseEntity = attendenceService.uploadFile(id, file);
			String message = responseEntity.getBody(); 
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else {
			return new ResponseEntity<String>("you are not allowed for this", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/downloadpdf/{id}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable("id") int id) {
		byte[] fileData = attendenceService.downloadFile(id);

		// Extract the actual Attendance entity from the ResponseEntity
        Optional<Attendence> attendenceOptional = attendenceService.getAllAttendenceById(id).getBody();
        Attendence attendence = attendenceOptional.orElseThrow(() -> 
            new ResourceNotFoundException("Attendence", "id", id));

        String dynamicFilename = attendence.getName()+"_"+attendence.getSubname()+".pdf"; 

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF); // Set content type to PDF
		headers.setContentDispositionFormData("attachment",dynamicFilename); // Set filename for download

		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
	}

	@DeleteMapping("/deleteattendencebyId/{id}")
	public ResponseEntity<?> deleteAttendencebyId(@PathVariable("id") int id,
			@RequestBody AttendenceDTO attendenceDTO) {

			
			return new ResponseEntity<>(attendenceService.delteAttendenceById(id, attendenceDTO),HttpStatus.OK);

		
	}

	@DeleteMapping("/deleteattendencebySubId/{subId}")
	public ResponseEntity<?> deleteAttendencebySubId(@PathVariable int subId,@RequestBody AttendenceDTO attendenceDTO) {

		
			return new ResponseEntity<>(attendenceService.deleteAllAttendenceSub(subId,attendenceDTO),HttpStatus.OK);

		
	}

	@DeleteMapping("/deleteAllattendence")
	public ResponseEntity<?> deleteAllAttendence(AttendenceDTO attendenceDTO) {

			
			return new ResponseEntity<>(attendenceService.deleteAllAttendence(attendenceDTO),HttpStatus.OK);

		
	}
}
