package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.AppointmentViewDto;
import com.dashboard.doctor_dashboard.dtos.NotificationDto;
import com.dashboard.doctor_dashboard.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import com.dashboard.doctor_dashboard.services.PatientService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PatientControllerTest {


    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void addPatientTest() throws Exception {
        Long id = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto("9728330045", Gender.Male,21, BloodGroup.Apositive,"Address1","9728330045");
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender(Gender.Male);
        patient.setBloodGroup(BloodGroup.Apositive);
        patient.setAlternateMobileNo("900011112");

        Mockito.when(patientService.addPatient(Mockito.any(PatientEntityDto.class),Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patient), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(patientEntityDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/patient/on-boarding/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    void getAppointmentViewByAppointmentIdTest() throws Exception {

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar", Category.General,LocalDate.now(),LocalTime.now(),"completed",BloodGroup.Bpositive, (short) 21,Gender.Male);


        Mockito.when(patientService.viewAppointment(Mockito.any(Long.class),Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,viewDto), HttpStatus.OK));


        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/1/appointment/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


    @Test
    void patientProfileTest() throws Exception {

        PatientEntityDto patientEntityDto = new PatientEntityDto("9728330045",Gender.Male,21,BloodGroup.Apositive,"Address1","9728330045");

        Mockito.when(patientService.getPatientDetailsById(Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientEntityDto),HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/patient-profile/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


    @Test
    void updatePatientTest() throws Exception {
        final Long id = 1L;
        String message = "Mobile No. Successfully Updated";

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(id,"9728330045");

        Mockito.when(patientService.updatePatientDetails(Mockito.any(Long.class),Mockito.any(UserDetailsUpdateDto.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message),HttpStatus.OK));

        String content = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/patient/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());


    }

    @Test
    void deletePatientByIdTest() throws Exception {
        Long id = 1L;

        patientController.deletePatientById(id);
        patientController.deletePatientById(id);

        verify(patientService,times(2)).deletePatientById(id);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/patient/private/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getNotifications() throws Exception {
        Long appointmentId = 1L;
        NotificationDto notificationDto = new NotificationDto(appointmentId,"Sagar");

        Mockito.when(patientService.getNotifications(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,notificationDto),HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/1/notifications").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}