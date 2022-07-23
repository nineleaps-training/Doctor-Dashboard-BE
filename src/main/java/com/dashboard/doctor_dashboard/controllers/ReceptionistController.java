package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
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
    /**
     * @return All the doctor names present in the database are returned with status code 200.
     * This endpoint is used for getting all doctor names.
     */
    @GetMapping("/doctorNames/")
    public ResponseEntity<GenericMessage> doctorNames(){
                log.info("ReceptionistController:: doctorNames");

        return receptionistService.getDoctorDetails();
    }

    /**
     * @param doctorId is used as path variable
     * @param pageNo is used as path variable
     * @return list of the today's appointments of the doctor with status code 200
     * This endpoint is used for getting the today's appointments of the doctor.
     */
    @GetMapping("/appointmentList/{doctorId}")
    public ResponseEntity<GenericMessage> appointmentList(@PathVariable("doctorId") long doctorId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
                log.info("ReceptionistController:: appointmentList");

        return receptionistService.getDoctorAppointments(doctorId,pageNo,pageSize);
    }

    /**
     * @param appointmentId is used as path variable
     * @param attributesDto is used as path variable
     * @return Vitals updated message after updating vitals in the database with status code 201.
     * This endpoint is used for adding patient vitals.
     */

    @PostMapping("/addVitals/{appointmentId}")
    public ResponseEntity<GenericMessage> addVitals(@Valid @RequestBody AttributesDto attributesDto,@PathVariable("appointmentId") Long appointmentId){
                log.info("ReceptionistController:: addVitals");

        return receptionistService.addAppointmentVitals(attributesDto,appointmentId);
    }


    /**
     * @param pageNo is used as path variable
     * @return All the today's appointments are returned with status code 200.
     * This endpoint is used for getting today's appointments of all the doctors.
     */
    @GetMapping("/getAllAppointments")
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
                log.info("ReceptionistController:: todayAllAppointmentForClinicStaff");

        return receptionistService.todayAllAppointmentForClinicStaff(pageNo,pageSize);
    }

}


