package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.FileDB;
import com.dashboard.doctor_dashboard.services.file.FileStorage;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private FileController fileController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();

        System.out.println("setting up");
    }



    @Test
    @DisplayName("Upload File")
    void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        String value = "Successful";

        Mockito.when(fileStorage.store(Mockito.any(MultipartFile.class),Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,value),HttpStatus.CREATED));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files/patient/1/upload")
                .file(file).contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isCreated());

    }




    //    @Test
//    void throwErrorIfFileNotStoredInDb() throws Exception {
//        final Long id = 1L;
//        FileDB fileDB = new FileDB();
//        fileDB.setDataReport(null);
//        fileDB.setId(id);
//        fileDB.setType(".png");
//        fileDB.setName("file1");
//        fileDB.setAppointmentId(id);
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "hello.txt",
//                MediaType.TEXT_PLAIN_VALUE,
//                "Hello, World!".getBytes()
//        );
//
//        String value = "Successful";
//        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseMessage(value));
//
//        String content = objectMapper.writeValueAsString(file.getBytes());
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/patient/upload/1")
//                .file(file).contentType(MediaType.MULTIPART_FORM_DATA)
//                .content(content)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void throwsException() throws IOException {
//        final Long id = 1L;
//
//        MultipartFile file = new MockMultipartFile(
//                "file",
//                "hello.txt",
//                MediaType.TEXT_PLAIN_VALUE,
//                "Hello, World!".getBytes()
//        );
//
//        Mockito.when(fileStorageService.store(file,id)).thenThrow(IOException.class);
//
//        ResponseEntity<GenericMessage> response = fileController.uploadFile(file,id);
//        assertEquals(HttpStatus.EXPECTATION_FAILED,response.getStatusCode());
//    }
//
//
    @Test
    @DisplayName("Download File")
    void testGetFileById() throws Exception {
        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(1L);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);


        ResponseEntity<byte[]> bytes = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getDataReport());

        Mockito.when(fileStorage.getFile(id)).thenReturn(bytes);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/files/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}