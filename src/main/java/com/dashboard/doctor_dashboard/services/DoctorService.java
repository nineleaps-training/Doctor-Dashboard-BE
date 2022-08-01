package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * interface for doctor service layer.
 */
@Service
public interface DoctorService {

    List<DoctorListDto> getAllDoctors(Long id);

    DoctorBasicDetailsDto getDoctorById(long id);

    //<<<<<<< HEAD
    DoctorFormDto updateDoctor(UserDetailsUpdateDto details, long id, HttpServletRequest request);
    DoctorFormDto addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request);

    String deleteDoctor(long id);

    PageRecords getAllDoctorsBySpeciality(String speciality, int pageNo, int pageSize);

    Map<String, Integer> genderChart(Long doctorId);

    Map<String, Integer> bloodGroupChart(Long doctorId);

    Map<String, Integer> ageGroupChart(Long doctorId);




}