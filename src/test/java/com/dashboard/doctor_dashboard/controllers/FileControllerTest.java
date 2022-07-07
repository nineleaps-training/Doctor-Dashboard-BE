package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.entities.report.ResponseMessage;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import com.dashboard.doctor_dashboard.exceptions.ReportNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void uploadFile_Success() throws IOException {
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

        String value = "Successful";
        ResponseEntity<ResponseMessage> messages = ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(value));

        Mockito.when(fileStorageService.store(file,id)).thenReturn(fileDB);

        ResponseEntity<GenericMessage> newMessage = fileController.uploadFile(file,id);

        assertThat(newMessage).isNotNull();
        assertEquals(newMessage.getStatusCode(),messages.getStatusCode());
    }

    @Test
    void throwErrorIfFileNotStoredInDb_Success() throws IOException {
        final Long id = 1L;
        FileDB fileDataB = new FileDB();
        fileDataB.setDataReport(null);
        fileDataB.setId(id);
        fileDataB.setType(".jpeg");
        fileDataB.setName("File");
        fileDataB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file_1",
                "hi.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hii!!!".getBytes()
        );

        String value = "failed";
        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(value));

        Mockito.when(fileStorageService.store(file,id)).thenReturn(null);

        ResponseEntity<GenericMessage> newMessage = fileController.uploadFile(file,id);

        assertThat(newMessage).isNotNull();
        assertEquals(newMessage.getStatusCode(),message.getStatusCode());
    }




//    @Test
//    @Disabled
//    void getListFiles() {
//
//        List<ResponseFile> files = new ArrayList<>();
//        ResponseFile file1 = new ResponseFile("file1",null,".png",10000);
//        ResponseFile file2 = new ResponseFile("file2",null,".png",10000);
//        files.addAll(Arrays.asList(file1,file2));
//
//
//        List<FileDB> report = new ArrayList<>();
//        final Long id = 1L;
//        FileDB fileDB = new FileDB();
//        fileDB.setDataReport(null);
//        fileDB.setId(1L);
//        fileDB.setType(".png");
//        fileDB.setName("file1");
//        fileDB.setPatientId(id);
//
//        FileDB fileDB1 = new FileDB();
//        fileDB1.setDataReport(null);
//        fileDB1.setId(2L);
//        fileDB1.setType(".png");
//        fileDB1.setName("file2");
//        fileDB1.setPatientId(id);
//
//        Mockito.when(fileStorageService.getAllFiles().map(dbFile -> {
//            ServletUriComponentsBuilder newValue = Mockito.any(ServletUriComponentsBuilder.class);
//            return new ResponseFile(
//                    dbFile.getName(),
//                    newValue.toString(),
//                    dbFile.getType(),
//                    dbFile.getDataReport().length);
//        }).collect(Collectors.toList())).thenReturn(files.stream().toList());
//
//        ResponseEntity<List<ResponseFile>> newFile = fileController.getListFiles();
//        System.out.println(newFile);
//
//
//
//
//
//    }


    @Test
    void throwsException_Success() throws IOException {
        final Long id = 1L;

        MultipartFile file1 = new MockMultipartFile(
                "File",
                "hi.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hii!!!".getBytes()
        );

        Mockito.when(fileStorageService.store(file1,id)).thenThrow(IOException.class);

        ResponseEntity<GenericMessage> response = fileController.uploadFile(file1,id);
        assertEquals(HttpStatus.EXPECTATION_FAILED,response.getStatusCode());
    }
    @Test
    void testGetFileById_Success() {
        final Long id = 1L;
        FileDB fileDataB = new FileDB();
        fileDataB.setDataReport(null);
        fileDataB.setId(1L);
        fileDataB.setType(".jpeg");
        fileDataB.setName("File");
        fileDataB.setAppointmentId(id);

        Mockito.when(fileStorageService.getFile(id)).thenReturn(fileDataB);

        ResponseEntity<byte[]> newFile = fileController.getFile(id);
        System.out.println(newFile.getStatusCodeValue());

        assertThat(newFile).isNotNull();
        assertEquals(200,newFile.getStatusCodeValue());

    }


//    @Test
//    void throwExceptionWhenIdNotPresentInDbForReport_Success() {
//        final Long id = 1L;
//
//        Mockito.when(fileStorageService.getFile(id)).thenReturn(null);
//
//        assertThrows(ResourceNotFound.class,() -> {
//            fileController.getFile(id);
//        });
//    }
}