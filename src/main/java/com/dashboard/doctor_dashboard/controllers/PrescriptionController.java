package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription.PrescriptionService;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<GenericMessage> addPrescription(@Valid @RequestBody UpdatePrescriptionDto updatePrescriptionDto, @PathVariable("appointId") Long appointId ) throws MessagingException, JSONException, IOException {
        log.info("PrescriptionController:: addPrescription");

        return prescriptionService.addPrescription(appointId,updatePrescriptionDto);
    }

    /**
     * @param appointId is used as path variable
     * @return List of prescription on the basis of appointment id provided with status code 200
     * This endpoint is used for getting all prescriptions of the patient.
     */
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage> allPrescriptions(@PathVariable("appointId") Long appointId) {
        log.info("PrescriptionController:: allPrescriptions");

        return prescriptionService.getAllPrescriptionByAppointment(appointId);
    }

    /**
     * @param id is used as path variable
     * @return Appointment deleted after successful api call with status code 200.
     * This endpoint is used for deleting the appointment.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<GenericMessage> deleteAppointment(@PathVariable("id") Long id) {
        log.info("PrescriptionController:: deleteAppointment");
        return prescriptionService.deleteAppointmentById(id);

    }

}