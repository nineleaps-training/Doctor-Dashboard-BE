package com.dashboard.doctor_dashboard.services.file_service;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
    ResponseEntity<GenericMessage> store(MultipartFile file, Long id);
    ResponseEntity<byte[]> getFile(Long id);
}
