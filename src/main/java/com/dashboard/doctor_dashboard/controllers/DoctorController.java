package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1612")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/get-all/doctor/{doctorId}")
    public ResponseEntity<List<DoctorListDto>> getAllDoctors(@PathVariable("doctorId") Long id) {

        List<DoctorListDto> details = doctorService.getAllDoctors(id);
        if (details != null)
//            return details;
            return new ResponseEntity<>(details,HttpStatus.OK);
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @GetMapping("/id/{id}")
    public DoctorBasicDetailsDto getDoctorById(@PathVariable("id") long id) {
        if (doctorService.getDoctorById(id) != null)
            return doctorService.getDoctorById(id);
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<DoctorFormDto> addDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        var doctorFormDto = doctorService.addDoctorDetails(details,id,request);
        if (doctorFormDto != null)
            return new ResponseEntity<>(doctorFormDto, HttpStatus.OK);
        throw new APIException(HttpStatus.BAD_REQUEST, "id mismatch");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorFormDto> updateDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        var doctorFormDto = doctorService.updateDoctor(details,id,request);
        if (doctorFormDto != null)
            return new ResponseEntity<>(doctorFormDto, HttpStatus.OK);
        throw new APIException(HttpStatus.BAD_REQUEST, "id mismatch");
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable("id") int id) {
        return doctorService.deleteDoctor(id);
    }

}
