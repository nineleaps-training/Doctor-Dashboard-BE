package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/prescription")
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/{appointId}")
    public ResponseEntity<GenericMessage> addPrescription(@PathVariable("appointId") Long appointId, @RequestBody List<Prescription> prescription) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(prescriptionService.addPrescription(appointId,prescription));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage> getALlPrescription(@PathVariable("appointId") Long appointId) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(prescriptionService.getAllPrescriptionByAppointment(appointId));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteAppointment(@PathVariable("id") Long id) {
        return prescriptionService.deleteAppointmentById(id);
    }

}