package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1612")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/doctor")
public class DoctorController {


    private DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/get-all/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllDoctors(@PathVariable("doctorId") Long id) {

        ResponseEntity<GenericMessage> details = doctorService.getAllDoctors(id);
        if (details != null)
            return details;
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GenericMessage> getDoctorById(@PathVariable("id") long id) {
            return doctorService.getDoctorById(id);
    }

    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<GenericMessage>  addDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ValidationsException(errors);
        }

        return doctorService.addDoctorDetails(details,id,request);

    }
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage>  updateDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody UserDetailsUpdateDto details, BindingResult bindingResult, HttpServletRequest request)  {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        return doctorService.updateDoctor(details,id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteDoctor(@PathVariable("id") int id) {
        return doctorService.deleteDoctor(id);
    }

    @GetMapping("/get-all-doctor/{speciality}")
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(@PathVariable("speciality") String speciality) {
        return doctorService.getAllDoctorsBySpeciality(speciality);
    }

    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> gender(@PathVariable("doctorId") Long doctorId) {
        return doctorService.genderChart(doctorId);
    }

    @GetMapping("/{doctorId}/bloodGroup")
    public ResponseEntity<GenericMessage> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        return doctorService.bloodGroupChart(doctorId);
    }

    @GetMapping("/{doctorId}/ageGroup")
    public ResponseEntity<GenericMessage> ageGroup(@PathVariable("doctorId") Long doctorId) {
        return doctorService.ageGroupChart(doctorId);
    }
}
