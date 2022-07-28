package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    ResponseEntity<GenericMessage> store(MultipartFile file, Long id);
    ResponseEntity<byte[]> getFile(Long id);
}