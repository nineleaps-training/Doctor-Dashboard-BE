package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.VitalsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReceptionistService {
    ResponseEntity<GenericMessage> getDoctorDetails(int pageNo);
    ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo);
  //  ResponseEntity<GenericMessage> updateAppointmentVitals(VitalsDto attributes, long appointmentId);
    ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo);

    ResponseEntity<GenericMessage> addAppointmentVitals(Attributes vitalsDto, Long appointmentId);

}
