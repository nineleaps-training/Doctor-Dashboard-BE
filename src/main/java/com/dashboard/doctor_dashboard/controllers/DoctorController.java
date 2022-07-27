package com.dashboard.doctor_dashboard.controllers;
import com.dashboard.doctor_dashboard.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.services.doctor.DoctorService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
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
    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }



    /**
     * This endpoint returns DoctorBasicDetailsDto which contains details  of the doctor.
     * @param loginId this variable contains loginId.
     * @return It returns a DoctorBasicDetailsDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/id/{loginId}")
    public ResponseEntity<GenericMessage> doctorById(@PathVariable("loginId") long loginId) {
        log.info("DoctorController:: doctorById");

        return doctorService.getDoctorById(loginId);
    }

    /**
     * This endpoint is used for doctor on-boarding.
     * @param doctorDetails this variable contains doctor details.
     * @param id this variable contains loginId.
     * @param request this object contains information related to user HTTP request.
     * @return It returns a DoctorFormDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     */
    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<GenericMessage>  addDoctorDetails(@Valid @RequestBody DoctorFormDto doctorDetails, @PathVariable("id") long id, HttpServletRequest request) {
        log.info("DoctorController:: addDoctorDetails");

        return doctorService.addDoctorDetails(doctorDetails,id,request);

    }

    /**
     * This endpoint is used for updating doctor details.
     * @param details this variable contains doctor to updated details.
     * @param id this variable contains loginId.
     * @param request this object contains information related to user HTTP request.
     * @return It returns updated doctor fields wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GenericMessage>  editDoctorDetails(@Valid @RequestBody UserDetailsUpdateDto details, @PathVariable("id") long id, HttpServletRequest request) {
        log.info("DoctorController:: updateDoctorDetails");

        return doctorService.updateDoctor(details,id,request);
    }

    /**
     * This endpoint is used for deleting doctor.
     * @param id this variable contains loginId.
     * @return A success message wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @DeleteMapping("/private/{id}")
    public ResponseEntity<GenericMessage> deleteDoctor(@PathVariable("id") int id) {
        log.info("DoctorController:: deleteDoctor");

        return doctorService.deleteDoctor(id);
    }

    /**
     * This endpoint returns a List<DoctorListDto> based on the speciality selected. It contains basic doctor details .
     * @param speciality this variable contains speciality.
     * @return It returns List<DoctorListDto> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/all-doctors/{speciality}")
    public ResponseEntity<GenericMessage> allDoctorsBySpeciality(@PathVariable("speciality") String speciality,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("DoctorController:: allDoctorsBySpeciality");

        return doctorService.getAllDoctorsBySpeciality(speciality,pageNo,pageSize);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains gender data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> gender(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: gender");

        return doctorService.genderChart(doctorId);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains blood group data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/blood-group")
    public ResponseEntity<GenericMessage> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: bloodGroup");

        return doctorService.bloodGroupChart(doctorId);
    }

    /**
     * This endpoint returns a Map<String,Integer> for a doctor, which contains age group's data of the patients retrieved by doctor loginId.
     * @param doctorId this variable contains doctorId.
     * @return It returns Map<String,Integer> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{doctorId}/age-group")
    public ResponseEntity<GenericMessage> ageGroup(@PathVariable("doctorId") Long doctorId) {
        log.info("DoctorController:: ageGroup");

        return doctorService.ageGroupChart(doctorId);
    }
}