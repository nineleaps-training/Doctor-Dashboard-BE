package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.entities.*;
import com.dashboard.doctor_dashboard.services.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/prescription")
@Slf4j
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService){
        this.prescriptionService = prescriptionService;
    }

    /**
     * @param appointId is used as path variable
     * @param updatePrescriptionDto contains field patientDto,prescription status,notes etc
     * @return a success message prescription updated after saving prescription in the database with status code 201
     * @throws MessagingException
     * @throws JSONException
     * @throws IOException
     * This endpoint is used for uploading prescription for patient.
     */
    @PostMapping("/{appointId}")
    public ResponseEntity<String> addPrescription(@Valid @RequestBody UpdatePrescriptionDto updatePrescriptionDto, @PathVariable("appointId") Long appointId ) throws MessagingException, JSONException, IOException {
        log.info("PrescriptionController:: addPrescription");

        return new ResponseEntity<>(prescriptionService.addPrescription(appointId,updatePrescriptionDto), HttpStatus.CREATED);
    }

    /**
     * @param appointId is used as path variable
     * @return List of prescription on the basis of appointment id provided with status code 200
     * This endpoint is used for getting all prescriptions of the patient.
     */
    @GetMapping("/{appointId}")
    public ResponseEntity<List<Prescription>> allPrescriptions(@PathVariable("appointId") Long appointId) {
        log.info("PrescriptionController:: allPrescriptions");

        return new ResponseEntity<>(prescriptionService.getAllPrescriptionByAppointment(appointId),HttpStatus.OK);
    }

    /**
     * @param id is used as path variable
     * @return Appointment deleted after successful api call with status code 200.
     * This endpoint is used for deleting the appointment.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") Long id) {
        log.info("PrescriptionController:: deleteAppointment");
        return new ResponseEntity<>(prescriptionService.deleteAppointmentById(id),HttpStatus.NO_CONTENT);

    }

}