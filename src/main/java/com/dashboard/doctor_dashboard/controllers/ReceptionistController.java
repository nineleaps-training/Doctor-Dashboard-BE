package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/receptionist")
@Slf4j
public class ReceptionistController {


    ReceptionistService receptionistService;
    @Autowired
    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    @GetMapping("/doctorNames/")
    public ResponseEntity<GenericMessage> doctorNames(){
        log.info("ReceptionistController::doctorNames");
        return receptionistService.getDoctorDetails();
    }

    @GetMapping("/appointmentList/{doctorId}")
    public ResponseEntity<GenericMessage> appointmentList(@PathVariable("doctorId") long doctorId, @RequestParam("pageNo") int pageNo){
        log.info("ReceptionistController::appointmentList");
        return receptionistService.getDoctorAppointments(doctorId,pageNo);
    }

    @PostMapping("/addVitals/{appointmentId}")
    public ResponseEntity<GenericMessage> addVitals(@Valid @RequestBody AttributesDto vitalsDto,@PathVariable("appointmentId") Long appointmentId){
        log.info("ReceptionistController:: addVitals");
        return receptionistService.addAppointmentVitals(vitalsDto,appointmentId);
    }

    @GetMapping("/getAllAppointments")
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(@RequestParam("pageNo") int pageNo){
        log.info("ReceptionistController::todayAllAppointmentForClinicStaff");
        return receptionistService.todayAllAppointmentForClinicStaff(pageNo);
    }

}


