package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.util.wrappers.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/prescription")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class PrescriptionController {


    private PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/{appointId}")
    public ResponseEntity<GenericMessage> addPrescription(@Valid @RequestBody UpdatePrescriptionDto updatePrescriptionDto,@PathVariable("appointId") Long appointId ) throws MessagingException, JSONException, IOException {
        log.info("PrescriptionController:: addPrescription");

       return prescriptionService.addPrescription(appointId,updatePrescriptionDto);

    }

    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage> getALlPrescription(@PathVariable("appointId") Long appointId) {
        log.info("PrescriptionController::getALlPrescription");
        return prescriptionService.getAllPrescriptionByAppointment(appointId);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteAppointment(@PathVariable("id") Long id) {
        log.info("PrescriptionController::deleteAppointment");
        return prescriptionService.deleteAppointmentById(id);
    }

}
