package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.services.ReceptionistService;
import com.dashboard.doctor_dashboard.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/receptionist")
@Slf4j
public class ReceptionistController {

    private final  ReceptionistService receptionistService;


    @Autowired
    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }
    /**
     * @return All the doctor names present in the database are returned with status code 200.
     * This endpoint is used for getting all doctor names.
     */
    @GetMapping("/doctor-names/")
    public ResponseEntity<List<DoctorDropdownDto>> doctorNames(){
        log.info("ReceptionistController:: doctorNames");

        return new ResponseEntity<>(receptionistService.getDoctorDetails(), HttpStatus.OK);
    }

    /**
     * @param doctorId is used as path variable
     * @param pageNo is used as path variable
     * @return list of the today's appointments of the doctor with status code 200
     * This endpoint is used for getting the today's appointments of the doctor.
     */
    @GetMapping("/appointment-list/{doctorId}")
    public ResponseEntity<PageRecords> appointmentList(@PathVariable("doctorId") long doctorId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
        log.info("ReceptionistController:: appointmentList");

        return new ResponseEntity<>( receptionistService.getDoctorAppointments(doctorId,pageNo,pageSize),HttpStatus.OK);
    }

    /**
     * @param appointmentId is used as path variable
     * @param attributesDto is used as path variable
     * @return Vitals updated message after updating vitals in the database with status code 201.
     * This endpoint is used for adding patient vitals.
     */

    @PostMapping("/vitals/{appointmentId}")
    public ResponseEntity<String> addVitals(@Valid @RequestBody AttributesDto attributesDto, @PathVariable("appointmentId") Long appointmentId){
        log.info("ReceptionistController:: addVitals");

        return new ResponseEntity<>(receptionistService.addAppointmentVitals(attributesDto,appointmentId),HttpStatus.CREATED);
    }


    /**
     * @param pageNo is used as path variable
     * @return All the today's appointments are returned with status code 200.
     * This endpoint is used for getting today's appointments of all the doctors.
     */
    @GetMapping("/all-appointments")
    public ResponseEntity<PageRecords> todayAllAppointmentForClinicStaff(@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
        log.info("ReceptionistController:: todayAllAppointmentForClinicStaff");

        return new ResponseEntity<>(receptionistService.todayAllAppointmentForClinicStaff(pageNo,pageSize),HttpStatus.NO_CONTENT);
    }

}

