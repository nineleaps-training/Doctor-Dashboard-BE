package com.dashboard.doctor_dashboard.services.file_service;

import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * interface for File Storage service layer.
 */
public interface FileStorage {
    ResponseEntity<GenericMessage> store(MultipartFile file, Long id);
    ResponseEntity<byte[]> getFile(Long id);
}


