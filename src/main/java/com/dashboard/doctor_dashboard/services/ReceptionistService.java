package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * interface for receptionist service layer.
 */
@Service
public interface ReceptionistService {
    List<DoctorDropdownDto> getDoctorDetails();
    PageRecords getDoctorAppointments(Long doctorId, int pageNo, int pageSize);
    PageRecords todayAllAppointmentForClinicStaff(int pageNo, int pageSize);

    String addAppointmentVitals(AttributesDto vitalsDto, Long appointmentId);

}