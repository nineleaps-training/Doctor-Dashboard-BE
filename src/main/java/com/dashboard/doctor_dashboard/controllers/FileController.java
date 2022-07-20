package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.report.ResponseMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private FileStorageService storageService;
    @Autowired
    public FileController(FileStorageService storageService){
        this.storageService = storageService;
    }

    /**
     * This endpoint is used for uploading patient medical reports.
     * @param file this variable contains file .
     * @param id this variable contains id.
     * @return A success message wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @ResponseBody
    @PostMapping("/api/v1/patient/upload/{id}")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id) {
        var genericMessage = new GenericMessage();

        var message = "";
        try {
            var fileDB = storageService.store(file, id);
            if (fileDB == null) {
                genericMessage.setData(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Patient ID not Found:" + id)));
                genericMessage.setStatus(Constants.FAIL);
                return new ResponseEntity<>(genericMessage,HttpStatus.BAD_REQUEST);
            }
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
             genericMessage.setData(ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(message)));
             genericMessage.setStatus(Constants.SUCCESS);
             return new ResponseEntity<>(genericMessage,HttpStatus.CREATED);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            genericMessage.setData(ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message)));
            genericMessage.setStatus(Constants.FAIL);
            return new ResponseEntity<>(genericMessage,HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * This endpoint is used for download the medical reports uploaded by the patient.
     * @param id this variable contains id.
     * @return A file wrapped under ResponseEntity<byte[]> with HTTP status code 200.
     */
    @GetMapping("/v1/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        try {
            var fileDB = storageService.getFile(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getDataReport());
        } catch (Exception e) {
            throw new ResourceNotFoundException("No Report Found.");
        }

    }
}