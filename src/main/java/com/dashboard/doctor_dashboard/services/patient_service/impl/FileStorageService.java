package com.dashboard.doctor_dashboard.services.patient_service.impl;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

/**
 *  Interface of AppointmentService layer
 */
@Service
public class FileStorageService {


    private FileDBRepository fileDBRepository;

    private AppointmentRepository appointmentRepository;

    @Autowired
    public FileStorageService(FileDBRepository fileDBRepository, AppointmentRepository appointmentRepository) {
        this.fileDBRepository = fileDBRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public FileDB store(MultipartFile file, Long id) throws IOException {

        Long temp = appointmentRepository.getId(id);
        if (temp == null) {
            return null;
        }
        String value = file.getOriginalFilename();
        if(value != null) {
            var fileName = StringUtils.cleanPath(value);
            var fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), id);
            return fileDBRepository.save(fileDB);
        }
        return null;
    }


    public FileDB getFile(Long id) {
        return fileDBRepository.findByAppointmentId(id);
    }


    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

}