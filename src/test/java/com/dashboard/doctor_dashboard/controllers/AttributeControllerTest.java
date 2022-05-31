package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.NotesDto;
import com.dashboard.doctor_dashboard.services.patient_service.AttributeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AttributeControllerTest {

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeController attributeController;

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
    void changePatientStatus() {
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Notes updated!!!");
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");
        Mockito.when(attributeService.changeNotes(Mockito.any(Long.class),
                Mockito.any(String.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> newNote = attributeController.updateNotes(1L,notesDto);

        assertEquals(message.getData(),newNote.getBody().getData());

    }
}