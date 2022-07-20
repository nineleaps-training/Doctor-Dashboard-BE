package com.dashboard.doctor_dashboard.services.doctor_service;

import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * interface for doctor service layer.
 */
@Service
public interface DoctorService {

    ResponseEntity<GenericMessage> getAllDoctors(Long id);

    ResponseEntity<GenericMessage> getDoctorById(long id);

//<<<<<<< HEAD
    ResponseEntity<GenericMessage>  updateDoctor(UserDetailsUpdateDto details, long id, HttpServletRequest request);
    ResponseEntity<GenericMessage>  addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request);

    ResponseEntity<GenericMessage> deleteDoctor(long id);

    ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality);

    ResponseEntity<GenericMessage> genderChart(Long doctorId);

    ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId);

    ResponseEntity<GenericMessage> ageGroupChart(Long doctorId);




}
