package com.dashboard.doctor_dashboard.services.patient_service.impl;


import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * interface for patient service layer.
 */
@Service
public interface PatientService {
    ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId);

    ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId);


    ResponseEntity<GenericMessage> deletePatientById(Long id);

    ResponseEntity<GenericMessage> updatePatientDetails(Long id, UserDetailsUpdateDto userDetailsUpdateDto);

    ResponseEntity<GenericMessage> viewAppointment(Long appointmentId, long patientId);

    ResponseEntity<GenericMessage> getNotifications(long patientId);

}