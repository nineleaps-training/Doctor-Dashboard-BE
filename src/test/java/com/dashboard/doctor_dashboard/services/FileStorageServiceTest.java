package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    @Mock
    private FileDBRepository fileDBRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private FileStorageService fileStorageService;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void UploadFileWhenPatientIdPresentInDb() throws IOException {


        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".jpeg");
        fileDB.setName("File");
        fileDB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file_1",
                "hi.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hi!!!".getBytes()
        );

        Mockito.when(appointmentRepository.getId(id)).thenReturn(id);
        Mockito.when(fileDBRepository.save(Mockito.any(FileDB.class))).thenReturn(fileDB);


        FileDB newFile = fileStorageService.store(file,id);
        assertThat(newFile).isNotNull();
        assertEquals(newFile,fileDB);
    }

    @Test
    void CheckIfFileNameIsPresentOrNot() throws IOException {

        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".jpeg");
        fileDB.setName("File");
        fileDB.setAppointmentId(id);

        MultipartFile file = mock(MultipartFile.class);

        Mockito.when(appointmentRepository.getId(id)).thenReturn(id);
        Mockito.when(file.getOriginalFilename()).thenReturn(null);

        FileDB newFile = fileStorageService.store(file,id);

        assertThat(newFile).isNull();
    }

    @Test
    void ThrowErrorWhenPatientIdNotPresentInDb() throws IOException {


        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".jpeg");
        fileDB.setName("File");
        fileDB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file_1",
                "hi.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hi!!!".getBytes()
        );

        Mockito.when(appointmentRepository.getId(id)).thenReturn(null);

        FileDB newFile = fileStorageService.store(file,id);

        assertThat(newFile).isNull();
        verify(fileDBRepository,never()).save(Mockito.any(FileDB.class));

    }

    @Test
    void getFile() {

        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".jpeg");
        fileDB.setName("File");
        fileDB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file_1",
                "hi.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hi!!!".getBytes()
        );

        Mockito.when(fileDBRepository.findByAppointmentId(id)).thenReturn(fileDB);

        FileDB newFiles = fileStorageService.getFile(id);

        assertThat(newFiles).isNotNull();
        assertEquals(newFiles,fileDB);
    }

}