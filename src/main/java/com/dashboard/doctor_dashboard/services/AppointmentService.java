package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.*;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

/**
 * interface for appointment service layer.
 */
@Service
public interface AppointmentService {
    Map<String, String> addAppointment(AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException;
    Map<String, PageRecords> getAllAppointmentByPatientId(Long patientId, int pageNo, int pageSize);
    Map<String, PageRecords> getAllAppointmentByDoctorId(Long doctorId, int pageNo, int pageSize );
    FollowUpDto getFollowDetails(Long appointId);


    PatientProfileDto getAppointmentById(Long appointId);
    List<DoctorAppointmentListDto> recentAppointment(Long doctorId);

    ArrayList<String> weeklyDoctorCountChart(Long doctorId);
    ArrayList<String> weeklyPatientCountChart(Long doctorId);


    int totalNoOfAppointment(Long doctorId);
    int todayAppointments(Long doctorId);
    int totalNoOfAppointmentAddedThisWeek(Long doctorId);
    ArrayList<String> patientCategoryGraph(Long patientId);



    List<Boolean> checkSlots(LocalDate date, Long doctorId);



}