package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ReportNotFound;
import com.dashboard.doctor_dashboard.services.file_service.FileStorageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class FileController {


    private FileStorageService storageService;
    @Autowired
    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }


    /**
     * This endpoint is used for uploading patient medical report.
     * @param file this variable contains file.
     * @param id this variable contains Id.
     * @return  A success message  wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @ApiOperation("This controller is for handling the API call to upload a file")
    @ResponseBody
   @PostMapping("/api/v1/patient/upload/{id}")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return storageService.store(file,id);
    }


    /**
     *  This endpoint is used for downloading the medical report uploaded by the patient.
     * @param id  this variable contains id.
     * @return A file wrapped under  ResponseEntity<GenericMessage> with HTTP status code 200.
     * @throws ReportNotFound
     */
    @ApiOperation("This API is responsible for downloading of file")
    @GetMapping("v1/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        return storageService.getFile(id);
        }

    }
