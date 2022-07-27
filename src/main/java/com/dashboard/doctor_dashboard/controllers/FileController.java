package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.services.file.FileStorage;
import com.dashboard.doctor_dashboard.services.file.FileStorageServiceImpl;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/files/")
@Slf4j
public class FileController {

    private FileStorage storageService;
    @Autowired
    public FileController(FileStorageServiceImpl storageService){
        this.storageService = storageService;
    }

    /**
     * This endpoint is used for uploading patient medical reports.
     * @param file this variable contains file .
     * @param id this variable contains id.
     * @return A success message wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @ResponseBody
    @PostMapping("patient/{id}/upload")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id){
        log.info("FileController::uploadFile");
        return storageService.store(file,id);
    }

    /**
     * This endpoint is used for download the medical reports uploaded by the patient.
     * @param id this variable contains id.
     * @return A file wrapped under ResponseEntity<byte[]> with HTTP status code 200.
     */
    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        log.info("FileController::getFile");
        return storageService.getFile(id);
    }
}