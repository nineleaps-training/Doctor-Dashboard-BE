package com.dashboard.doctor_dashboard.services.file;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorage {


    private FileDBRepository fileDBRepository;




    private AppointmentRepository appointmentRepository;

    @Autowired
    public FileStorageServiceImpl(FileDBRepository fileDBRepository, AppointmentRepository appointmentRepository) {
        this.fileDBRepository = fileDBRepository;
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public ResponseEntity<GenericMessage>  store(MultipartFile file, Long id)  {

        var message = "";
        if (appointmentRepository.getId(id) == null) {
            throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
        }
        String value = file.getOriginalFilename();
        if(value == null){
            throw new ResourceNotFoundException(Constants.FILE_NAME_PRESENT);
        }
        try {
            var fileName = StringUtils.cleanPath(value);
            var fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), id);
            fileDBRepository.save(fileDB);
            message = Constants.FILE_UPLOADED + file.getOriginalFilename();
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Error:"+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> getFile(Long id) {
        try {
            var fileDB = fileDBRepository.findByAppointmentId(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getDataReport());
        } catch (Exception e) {
            throw new ResourceNotFoundException(Constants.REPORT_NOT_FOUND);
        }
    }
}
