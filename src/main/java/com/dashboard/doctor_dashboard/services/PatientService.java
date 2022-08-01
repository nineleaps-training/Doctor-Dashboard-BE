package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * interface for patient service layer.
 */
@Service
public interface PatientService {
    PatientEntityDto addPatient(PatientEntityDto patient, Long loginId);

    PatientEntityDto getPatientDetailsById(Long loginId);


    String deletePatientById(Long id);

    String updatePatientDetails(Long id, UserDetailsUpdateDto userDetailsUpdateDto);

    AppointmentViewDto viewAppointment(Long appointmentId, long patientId);

    List<NotificationDto> getNotifications(long patientId);

}