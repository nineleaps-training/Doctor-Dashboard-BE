package com.dashboard.doctor_dashboard.controllers;
import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.services.DoctorService;
import com.dashboard.doctor_dashboard.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@SuppressWarnings("squid:S1612")
@RestController
@RequestMapping("/api/v1/doctor")
@Slf4j
public class DoctorController {


    private final DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }



    /**
     * This endpoint returns DoctorBasicDetailsDto which contains details  of the doctor.
     * @param loginId this variable contains loginId.
     * @return It returns a DoctorBasicDetailsDto wrapped under ResponseEntity<DoctorBasicDetailsDto> with HTTP status code 200.
     */
    @GetMapping("/id/{loginId}")
    public ResponseEntity<DoctorBasicDetailsDto> doctorById(@PathVariable("loginId") long loginId) {
        log.info("DoctorController:: doctorById");
        return new ResponseEntity<>(doctorService.getDoctorById(loginId), HttpStatus.OK);
    }

    /**
     * This endpoint is used for doctor on-boarding.
     * @param doctorDetails this variable contains doctor details.
     * @param id this variable contains loginId.
     * @param request this object contains information related to user HTTP request.
     * @return It returns a DoctorFormDto wrapped under ResponseEntity<DoctorFormDto> with HTTP status code 201.
     */
    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<DoctorFormDto>  addDoctorDetails(@Valid @RequestBody DoctorFormDto doctorDetails, @PathVariable("id") long id, HttpServletRequest request) {
        log.info("DoctorController:: addDoctorDetails");

        return new ResponseEntity<>(doctorService.addDoctorDetails(doctorDetails,id,request),HttpStatus.CREATED);

    }

    /**
     * This endpoint is used for updating doctor details.
     * @param details this variable contains doctor to updated details.
     * @param id this variable contains loginId.
     * @param request this object contains information related to user HTTP request.
     * @return It returns updated doctor fields wrapped under  ResponseEntity<DoctorFormDto> with HTTP status code 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorFormDto>  editDoctorDetails(@Valid @RequestBody UserDetailsUpdateDto details, @PathVariable("id") long id, HttpServletRequest request) {
        log.info("DoctorController:: updateDoctorDetails");

        return new ResponseEntity<>(doctorService.updateDoctor(details,id,request),HttpStatus.OK);
    }

    /**
     * This endpoint is used for deleting doctor.
     * @param id this variable contains loginId.
     * @return A success message wrapped under ResponseEntity<String> with HTTP status code 200.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable("id") int id) {
        log.info("DoctorController:: deleteDoctor");

        return new ResponseEntity<>(doctorService.deleteDoctor(id),HttpStatus.NO_CONTENT);
    }

    /**
     * This endpoint returns a List<DoctorListDto> based on the speciality selected. It contains basic doctor details .
     * @param speciality this variable contains speciality.
     * @return It returns List<DoctorListDto> wrapped under ResponseEntity<PageRecords> with HTTP status code 200.
     */
    @GetMapping("/all-doctors/{speciality}")
    public ResponseEntity<PageRecords> allDoctorsBySpeciality(@PathVariable("speciality") String speciality,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("DoctorController:: allDoctorsBySpeciality");

        return new ResponseEntity<>( doctorService.getAllDoctorsBySpeciality(speciality,pageNo,pageSize),HttpStatus.OK);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains gender data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<Map<String, Integer>> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<Map<String, Integer>> gender(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: gender");

        return new ResponseEntity<>(doctorService.genderChart(doctorId),HttpStatus.OK);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains blood group data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<Map<String, Integer>> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/blood-group")
    public ResponseEntity<Map<String, Integer>> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: bloodGroup");

        return new ResponseEntity<>( doctorService.bloodGroupChart(doctorId),HttpStatus.OK);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains age group's data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<Map<String, Integer>> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/age-group")
    public ResponseEntity<Map<String, Integer>> ageGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: ageGroup");

        return new ResponseEntity<>(doctorService.ageGroupChart(doctorId),HttpStatus.OK);
    }
}