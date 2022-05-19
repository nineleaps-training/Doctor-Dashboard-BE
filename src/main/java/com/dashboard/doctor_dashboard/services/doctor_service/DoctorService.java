package com.dashboard.doctor_dashboard.services.doctor_service;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface DoctorService {
    DoctorDetails addDoctor(DoctorDetails details);

    List<DoctorListDto> getAllDoctors(Long id);

    DoctorBasicDetailsDto getDoctorById(long id);

    DoctorFormDto updateDoctor(DoctorFormDto details, long id, HttpServletRequest request);
    DoctorFormDto addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request);

    String deleteDoctor(long id);
}
