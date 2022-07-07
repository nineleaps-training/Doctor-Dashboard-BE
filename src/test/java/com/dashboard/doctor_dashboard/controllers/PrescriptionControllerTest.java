package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Util.Constants;
import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PrescriptionControllerTest {

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

//    @Test
//    void getAddPrescriptionTest_Success() throws MessagingException, JSONException, IOException, IOException {
//        final Long id =1L;
//        PatientDto patientDto = new PatientDto("dentist","Dr.sagar","completed","Gokul","gokul123@gmail.com",21,"male");
//        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,null,"completed","full zzcheckup",true,1L);
//
//        Mockito.when(prescriptionService.addPrescription(Mockito.any(Long.class),Mockito.any(UpdatePrescriptionDto.class))).thenReturn(
//                String.valueOf(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,details), HttpStatus.OK)));
//
//        ResponseEntity<GenericMessage> newDetails = prescriptionController.addPrescription(id,details);
//        assertThat(newDetails).isNotNull();
//        assertEquals(details,newDetails.getBody().getData());
//    }

//    @Test
//    void getALlPrescriptionTest_Success() {
//        final Long appointId = 1L;
//        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,null);
//        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,null);
//
//        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));
//
//        Mockito.when(prescriptionService.getAllPrescriptionByAppointment(Mockito.any(Long.class))).thenReturn(
//                (List<Prescription>) new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,prescriptions), HttpStatus.OK));
//
//        ResponseEntity<GenericMessage> newPrescription = prescriptionController.getALlPrescription(appointId);
//        assertThat(newPrescription).isNotNull();
//        assertEquals(prescriptions,newPrescription.getBody().getData());
//
//    }

    @Test
    void getDeleteAppointmentTest_Success() {
        Long id = 1L;
        String message = "Successfully deleted";

        Mockito.when(prescriptionService.deleteAppointmentById(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.OK));

        ResponseEntity<GenericMessage> newMessage = prescriptionController.deleteAppointment(id);
        assertThat(message).isNotNull();
        assertEquals(message,newMessage.getBody().getData());


    }
}