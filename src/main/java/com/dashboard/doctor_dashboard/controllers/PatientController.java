package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.services.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/v1/patient")
@Slf4j
public class PatientController {


    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {

        this.patientService = patientService;
    }

    /**
     * @param patient consists field mobileNo,gender,age,bloodGroup,address and alternative mobile no
     * @param loginId is used as a path variable
     * @return patient added successfully after successful api call with status code 201
     * This endpoint is used for patient on-boarding.
     */
    @PostMapping("/on-boarding/{loginId}")
    public ResponseEntity<PatientEntityDto> onBoarding(@Valid @RequestBody PatientEntityDto patient, @PathVariable("loginId") Long loginId) {
        log.info("PatientController:: patientOnBoarding");

        return new ResponseEntity<>(patientService.addPatient(patient,loginId), HttpStatus.CREATED);
    }


    /**
     * @param patientId is used as a path variable
     * @param appointmentId is used as a path variable
     * @return appointment details from appointment database with status code 200
     * This endpoint is used for viewing the appointment for patient
     */
    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<AppointmentViewDto> appointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId) {
        log.info("PatientController:: appointmentViewByAppointmentId");

        return new ResponseEntity<>(patientService.viewAppointment(appointmentId,patientId),HttpStatus.OK);
    }

    /**
     * @param loginId is used as a path variable
     * @return patient details from patient database with status code 200
     * This endpoint for viewing the patient profile.
     */
    @GetMapping("/patient-profile/{loginId}")
    public ResponseEntity<PatientEntityDto> patientProfile(@PathVariable("loginId") Long loginId) {
        log.info("PatientController:: patientProfile");

        return new ResponseEntity<>(patientService.getPatientDetailsById(loginId),HttpStatus.OK);
    }

    /**
     * @param id is used as a path variable
     * @param userDetailsUpdateDto consist fields id and mobile number
     * @return patient updated successfully after successful api call with status code 200
     * This endpoint is used for updating patient profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> editPatientDetails(@Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto, @PathVariable("id") Long id) {
        log.info("PatientController:: editPatientDetails");

        return new ResponseEntity<>(patientService.updatePatientDetails(id, userDetailsUpdateDto),HttpStatus.OK);
    }
    /**
     * @param id is used as a path variable
     * @return Patient deleted after successful API call with status code 200
     * This endpoint is used for deleting patient details.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") Long id) {
        log.info("PatientController:: deletePatientById");

        return new ResponseEntity<>( patientService.deletePatientById(id),HttpStatus.NO_CONTENT);
    }

    /**
     * @param patientId is used as a path variable
     * @return list of notifications for the patient with status code 200
     * This endpoint is used for getting patient notifications.
     */
    @GetMapping("/{patientId}/notifications")
    public ResponseEntity<List<NotificationDto>> notifications(@PathVariable("patientId") Long patientId) {
        log.info("PatientController:: notifications");
        return new ResponseEntity<>(patientService.getNotifications(patientId),HttpStatus.OK);
    }

}