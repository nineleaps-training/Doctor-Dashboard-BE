package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * interface for receptionist service layer.
 */
@Service
public interface ReceptionistService {
    ResponseEntity<GenericMessage> getDoctorDetails();
    ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo,int pageSize);
    ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo,int pageSize);

    ResponseEntity<GenericMessage> addAppointmentVitals(AttributesDto vitalsDto, Long appointmentId);

}