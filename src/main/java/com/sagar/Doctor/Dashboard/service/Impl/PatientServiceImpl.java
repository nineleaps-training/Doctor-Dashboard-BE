package com.sagar.Doctor.Dashboard.service.Impl;

import com.sagar.Doctor.Dashboard.entity.Patient;
import com.sagar.Doctor.Dashboard.repository.PatientRepository;
import com.sagar.Doctor.Dashboard.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;


    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }


    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient value = patientRepository.findById(id).get();
        value.setFullName(patient.getFullName());
        value.setAge(patient.getAge());
        value.setDateOfBirth(patient.getDateOfBirth());
        value.setEmailId(patient.getEmailId());
        value.setGender(patient.getGender());

        return patientRepository.save(value);
    }

    @Override
    public void deletePatientById(Long id) {

        patientRepository.deleteById(id);
    }

}
