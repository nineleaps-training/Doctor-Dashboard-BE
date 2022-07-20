package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/patient")
@Slf4j
public class PatientController {



    private PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;

    }




    //CRUD operation for patient
    @PostMapping("/{loginId}")
    public ResponseEntity<GenericMessage> addPatientDetails(@Valid @RequestBody PatientEntityDto patient, @PathVariable("loginId") Long loginId) {
        log.info("PatientController:: addPatientDetails");
        return patientService.addPatient(patient,loginId);
    }

    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<GenericMessage> getAppointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId) {
        log.info("PatientController:: getAppointmentViewByAppointmentId");

        return patientService.viewAppointment(appointmentId,patientId);
    }

    @GetMapping("/patientProfile/{loginId}")
    public ResponseEntity<GenericMessage> patientProfile(@PathVariable("loginId") Long loginId) {
        log.info("PatientController:: patientProfile");
        return patientService.getPatientDetailsById(loginId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage> updatePatientDetails( @Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto,@PathVariable("id") Long id) {
        log.info("PatientController:: updatePatientDetails");

        return patientService.updatePatientDetails(id, userDetailsUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deletePatientById(@PathVariable("id") Long id) {
        log.info("PatientController::deletePatientById");
        return patientService.deletePatientById(id);
    }

    @GetMapping("/{patientId}/getNotifications")
    public  ResponseEntity<GenericMessage> getNotifications(@PathVariable("patientId") Long patientId) {
        log.info("PatientController:: getNotifications");
        return patientService.getNotifications(patientId);
    }

}
