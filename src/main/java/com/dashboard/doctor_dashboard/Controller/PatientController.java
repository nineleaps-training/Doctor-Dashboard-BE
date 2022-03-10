package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.Patient;
import com.dashboard.doctor_dashboard.Service.patient_service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/patient")

public class PatientController {


    @Autowired
    private PatientService patientService;


    @PostMapping()
    public Patient addPatient(@RequestBody Patient patient){
        return patientService.addPatient(patient);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Patient> getAllPatientsByDoctorId(@PathVariable("doctorId") Long doctorId ){
        return patientService.getAllPatientByDoctorId(doctorId);
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable("id") Long id){
        return  patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable("id") Long id,@RequestBody Patient patient){
        return patientService.updatePatient(id,patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatientById(@PathVariable("id") Long id) {

        patientService.deletePatientById(id);

    }

    @GetMapping("/{doctorId}/totalPatient")
    public int totalPatient(@PathVariable("doctorId") Long doctorId){
        return patientService.totalNoOfPatient(doctorId);
    }

    @GetMapping("/{doctorId}/category")
    public ArrayList<String> patientCategory(@PathVariable("doctorId") Long doctorId){
        return patientService.patientCategory(doctorId);
    }

    @GetMapping("/{doctorId}/gender")
    public ArrayList<String> gender(@PathVariable("doctorId") Long doctorId){
        return patientService.gender(doctorId);
    }

    @GetMapping("/{doctorId}/activePatient")
    public ArrayList<String> activePatient(@PathVariable("doctorId") Long doctorId){
        return patientService.activePatient(doctorId);
    }



}