package com.sagar.Doctor.Dashboard.service;

import com.sagar.Doctor.Dashboard.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {
    public Patient addPatient(Patient patient);
    public List<Patient> getAllPatient();
    public Patient getPatientById(Long id);
    public Patient updatePatient(Long id,Patient patient);
    public void deletePatientById(Long id);

}