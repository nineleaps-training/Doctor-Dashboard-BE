package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.util.wrappers.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@SuppressWarnings("squid:S1612")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/doctor")
@Slf4j
public class DoctorController {




    private DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


//    @GetMapping("/get-all/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getAllDoctors(@PathVariable("doctorId") Long id) {
//        log.info("AppointmentController::addAppointment");
//        ResponseEntity<GenericMessage> details = doctorService.getAllDoctors(id);
//        if (details != null)
//            return details;
//        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
//    }


    /**
     * This endpoint returns DoctorBasicDetailsDto which contains details of the doctor based
     * @param loginId this variable contains loginId.
     * @return  It returns int  wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/id/{loginId}")
    public ResponseEntity<GenericMessage> getDoctorById(@PathVariable("loginId") long loginId) {
        log.info("DoctorController:: getDoctorById");

        return doctorService.getDoctorById(loginId);
    }

    /**
     * This endpoint is used for doctor on-boarding.
     * @param doctorDetails this variable contains doctor details.
     * @param id this variable contains id.
     * @param request this object contains information related to user HTTP request.
     * @return  It returns DoctorFormDto  wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<GenericMessage>  addDoctorDetails( @Valid @RequestBody DoctorFormDto doctorDetails,@PathVariable("id") long id,HttpServletRequest request) {
        log.info("DoctorController:: addDoctorDetails");
        return doctorService.addDoctorDetails(doctorDetails,id,request);
    }

    /**
     * This endpoint is used for uploading doctor details.
     * @param id this variable contains id.
     * @param details this variable contains details.
     * @param request this variable contains requests.
     * @return  It returns updated doctor fields  wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage>  updateDoctorDetails( @Valid @RequestBody UserDetailsUpdateDto details,@PathVariable("id") long id, HttpServletRequest request) {
        log.info("DoctorController:: updateDoctorDetails");

        return doctorService.updateDoctor(details,id,request);
    }

    /**
     * This is used for deleting doctor.
     * @param id this variable contains id.
     * @return A success message wrapped under  ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteDoctor(@PathVariable("id") int id) {
        log.info("DoctorController::deleteDoctor");
        return doctorService.deleteDoctor(id);
    }

    /**
     * This endpoint returns a List<DoctorListDto> based on speciality selected.It contains basic doctor details.
     * @param speciality  this variable contains speciality.
     * @return It returns  List<DoctorListDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/get-all-doctor/{speciality}")
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(@PathVariable("speciality") String speciality) {
        log.info("DoctorController::getAllDoctorsBySpeciality");
        return doctorService.getAllDoctorsBySpeciality(speciality);
    }

    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> gender(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController::gender");
        return doctorService.genderChart(doctorId);
    }

    /**
     * This endpoint returns a  Map<String,Integer> for a doctor, which contains gender data of the patients received by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under  ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/bloodGroup")
    public ResponseEntity<GenericMessage> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController::bloodGroup");
        return doctorService.bloodGroupChart(doctorId);
    }

    /**
     *  This endpoint returns a  Map<String,Integer> for a doctor, which contains blood group data of the patients received by doctor loginId.
     * @param doctorId  this variable contains doctorId.
     * @return  It returns Map<String,Integer> wrapped under  ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/ageGroup")
    public ResponseEntity<GenericMessage> ageGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController::ageGroup");
        return doctorService.ageGroupChart(doctorId);
    }
}
