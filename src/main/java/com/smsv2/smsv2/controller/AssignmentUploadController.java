package com.smsv2.smsv2.controller;

import com.smsv2.smsv2.DTO.AssignmentUploadDTO;
import com.smsv2.smsv2.entity.AssignmentUpload;
import com.smsv2.smsv2.exception.ResourceNotFoundException;
import com.smsv2.smsv2.service.AssignmentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignmentUploads")
public class AssignmentUploadController {

    @Autowired
    private AssignmentUploadService assignmentUploadService;

    @GetMapping("/allAssignmentUpload")
    public ResponseEntity<?> getAllAssignmentUploads() {
        return new ResponseEntity<>(assignmentUploadService.getAllAssignmentUpload(),HttpStatus.OK);
    }

    @GetMapping("/assignmentUploadById/{id}")
    public ResponseEntity<?> getAssignmentUploadById(@PathVariable int id) {
    	return new ResponseEntity<>(assignmentUploadService.getAllAssignmentUploadById(id),HttpStatus.OK);
    }

    @GetMapping("/assignmentUploadByAssignmentId/{assignmentId}")
    public ResponseEntity<?> getAssignmentUploadsByAssignmentId(@PathVariable int assignmentId) {
    	return new ResponseEntity<>(assignmentUploadService.getAllAssignmentUploadByAssignmentId(assignmentId),HttpStatus.OK);
    }

    @GetMapping("/assignmentUploadByTeacherId/{teacherId}")
    public ResponseEntity<?> getAssignmentUploadsByTeacherId(@PathVariable int teacherId) {
    	return new ResponseEntity<>(assignmentUploadService.getAllAssignmentUploadByTeacherId(teacherId),HttpStatus.OK);
    }

//    @PostMapping("/uploadPdf/{id}")
//    public ResponseEntity<String> uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
//        return assignmentUploadService.uploadFile(id, file);
//    }

    @PostMapping("/addAssignmentUpload")
    public ResponseEntity<?> addAssignmentUpload(@ModelAttribute AssignmentUploadDTO assignmentUploadDTO, @RequestParam("file") MultipartFile file) {
    
    	return new ResponseEntity<>(assignmentUploadService.addAssignmentUpload(assignmentUploadDTO, file),HttpStatus.CREATED);
    }

    @PutMapping("/updateAssignmentUpload/{id}")
    public ResponseEntity<?> updateAssignmentUpload(@PathVariable int id, @ModelAttribute AssignmentUploadDTO assignmentUploadDTO, @RequestParam(value = "annotatedPdf", required = true) MultipartFile annotatedPdf) {
    	
    	return new ResponseEntity<>(assignmentUploadService.updateAssignmentUpload(id, assignmentUploadDTO, annotatedPdf),HttpStatus.OK);
    }

    @DeleteMapping("/deleteAssignmentUploadById/{id}")
    public ResponseEntity<?> deleteAssignmentUploadById(@PathVariable int id, @RequestBody AssignmentUploadDTO assignmentUploadDTO) {
       
        return new ResponseEntity<>(assignmentUploadService.delteAssignmentUploadById(id, assignmentUploadDTO),HttpStatus.OK);
    }

    @DeleteMapping("/deleteAssignmentUploadByAssignmentId/{assignmentId}")
    public ResponseEntity<?> deleteAllAssignmentUploadsByAssignmentId(@PathVariable int assignmentId, @RequestBody AssignmentUploadDTO assignmentUploadDTO) {
        
        return new ResponseEntity<>(assignmentUploadService.deleteAllAssignmentByAssignmentId(assignmentId, assignmentUploadDTO),HttpStatus.OK);
    }

    @DeleteMapping("/deleteAssignmentUploadAll")
    public ResponseEntity<?> deleteAllAssignmentUploads(@RequestBody AssignmentUploadDTO assignmentUploadDTO) {
    	
    	
    	return new ResponseEntity<>(assignmentUploadService.deleteAllAssignmentUpload(assignmentUploadDTO),HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable int id) {
        byte[] fileData = assignmentUploadService.downloadFile(id);
        
        // Extract the actual AssignmentUpload entity from the ResponseEntity
        Optional<AssignmentUpload> assignmentUploadOptional = assignmentUploadService.getAllAssignmentUploadById(id).getBody();
        AssignmentUpload assignmentUpload = assignmentUploadOptional.orElseThrow(() -> 
            new ResourceNotFoundException("AssignmentUpload", "id", id));

        String dynamicFilename = assignmentUpload.getAssignmentName()+"_"+assignmentUpload.getReg() + "_submission.pdf"; 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF); 
        headers.setContentDispositionFormData("attachment", dynamicFilename); // Set dynamic filename for download

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }


}
