package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.services.PatientService;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
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
    public ResponseEntity<GenericMessage> onBoarding(@Valid @RequestBody PatientEntityDto patient, @PathVariable("loginId") Long loginId) {
        log.info("PatientController:: patientOnBoarding");

        return patientService.addPatient(patient,loginId);
    }


    /**
     * @param patientId is used as a path variable
     * @param appointmentId is used as a path variable
     * @return appointment details from appointment database with status code 200
     * This endpoint is used for viewing the appointment for patient
     */
    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<GenericMessage> appointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId) {
        log.info("PatientController:: appointmentViewByAppointmentId");

        return patientService.viewAppointment(appointmentId,patientId);
    }

    /**
     * @param loginId is used as a path variable
     * @return patient details from patient database with status code 200
     * This endpoint for viewing the patient profile.
     */
    @GetMapping("/patient-profile/{loginId}")
    public ResponseEntity<GenericMessage> patientProfile(@PathVariable("loginId") Long loginId) {
        log.info("PatientController:: patientProfile");

        return patientService.getPatientDetailsById(loginId);
    }

    /**
     * @param id is used as a path variable
     * @param userDetailsUpdateDto consist fields id and mobile number
     * @return patient updated successfully after successful api call with status code 200
     * This endpoint is used for updating patient profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GenericMessage> editPatientDetails( @Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto,@PathVariable("id") Long id) {
        log.info("PatientController:: editPatientDetails");

        return patientService.updatePatientDetails(id, userDetailsUpdateDto);
    }
    /**
     * @param id is used as a path variable
     * @return Patient deleted after successful API call with status code 200
     * This endpoint is used for deleting patient details.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<GenericMessage> deletePatientById(@PathVariable("id") Long id) {
        log.info("PatientController:: deletePatientById");

        return patientService.deletePatientById(id);
    }

    /**
     * @param patientId is used as a path variable
     * @return list of notifications for the patient with status code 200
     * This endpoint is used for getting patient notifications.
     */
    @GetMapping("/{patientId}/notifications")
    public ResponseEntity<GenericMessage> notifications(@PathVariable("patientId") Long patientId) {
        log.info("PatientController:: notifications");
        return patientService.getNotifications(patientId);
    }

}
