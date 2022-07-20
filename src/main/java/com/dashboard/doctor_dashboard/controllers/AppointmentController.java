package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.AppointmentDto;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;


@RestController
@RequestMapping("api/v1/appointment")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AppointmentController {

    private AppointmentService appointmentService;


    @Autowired
    public AppointmentController(AppointmentService appointmentService){

        this.appointmentService = appointmentService;
    }

    /**
     * This endpoint is used to book an appointment
     * @param appointment this object contains appointment details.
     * @param request this object contains information related to user HTTP request.
     * @return A success message is returned wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/patient")
    public ResponseEntity<GenericMessage> addAppointment(@Valid  @RequestBody AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        log.info("AppointmentController:: addAppointment");
        return appointmentService.addAppointment(appointment,request);

    }

    /**
     * This endpoint creates a List<Boolean> with available time slots of the doctor on a given date.

     * @param date this variable contains date.
     * @param loginId this variable contains loginId.
     * @return It returns a List<Boolean> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
       @GetMapping("/getAvailableSlots/{loginId}/{date}")
    public ResponseEntity<GenericMessage> showAvailableSlots(@PathVariable("date") String  date,@PathVariable("loginId") Long loginId) {
           log.info("AppointmentController:: showAvailableSlots");
         return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentService.checkSlots(LocalDate.parse(date),loginId)),HttpStatus.OK);
    }

    /**
     * This endpoint creates a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming of the patient based on patientId

     * @param patientId this variable contains patientId.
     * @param pageNo this variable contains pageNo.
     * @return It returns a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/getAllAppointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId,@RequestParam("pageNo") int pageNo) {
        log.info("AppointmentController:: getAllAppointmentByPatientId");
        return appointmentService.getAllAppointmentByPatientId(patientId,pageNo) ;
    }

    /**
     * This endpoint creates a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming of the doctor based on loginId.
     * @param loginId this variable contains loginId.
     * @param pageNo this variable contains pageNo.
     * @return It returns a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/getAllAppointments/doctor/{loginId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("loginId") Long loginId,@RequestParam("pageNo") int pageNo) {
        log.info("AppointmentController:: getAllAppointmentByDoctorId");

        return appointmentService.getAllAppointmentByDoctorId(loginId, pageNo);
    }


    /**
     * This endpoint creates a PatientProfileDto based on appointmentId for patient to view an appointment.
     * @param appointId this variable contains appointmentId.
     * @return It returns a PatientProfileDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{appointId}/patient")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GenericMessage> getAppointmentById(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController:: getAppointmentById");

        return appointmentService.getAppointmentById(appointId);
    }

    /**
     * This endpoint creates a ArrayList<String> based on loginId for doctor to view weekly appointments count in a month.

     * @param loginId this variable contains loginId.
     * @return It returns a ArrayList<String> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("chart/{loginId}/weeklyGraphDoctor")
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: weeklyDoctorCountChart");

        return appointmentService.weeklyDoctorCountChart(loginId);
    }

    /**
     * This endpoint creates a ArrayList<String> based on patientId, to view weekly appointments booked by a patient in a month.

     * @param patientId this variable contains patientId.
     * @return It returns a ArrayList<String> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("chart/{patientId}/weeklyGraphPatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        log.info("AppointmentController:: weeklyPatientCountChart");

        return appointmentService.weeklyPatientCountChart(patientId);
    }


    /**
     * This endpoint creates a List<DoctorAppointmentListDto> it contains upcoming appointments of the doctor of the current day with limit 3.

     * @param loginId loginId this variable contains loginId.
     * @return It returns a List<DoctorAppointmentListDto> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/recentAdded/doctor/{loginId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: recentAppointment");

        return appointmentService.recentAppointment(loginId);
    }


    /**
     * This endpoint returns an int of total number of appointments of the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/totalPatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointment");

        return appointmentService.totalNoOfAppointment(loginId);
    }

    /**
     * This endpoint returns an int of total number of appointments for current day, for the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/todayAppointments")
    public ResponseEntity<GenericMessage> todayAppointments(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: todayAppointments");

        return appointmentService.todayAppointments(loginId);
    }

    /**
     * This endpoint returns an int of total number of appointments added in the current week, for the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointmentAddedThisWeek");

        return appointmentService.totalNoOfAppointmentAddedThisWeek(loginId);
    }

    /**
     * This endpoint returns an ArrayList<String> of categories and their count, which are appointments booked by patient with different category doctors.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{loginId}/category")
    public ResponseEntity<GenericMessage> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: patientCategoryGraph");

        return appointmentService.patientCategoryGraph(loginId);
    }

    /**
     * This endpoint returns an FollowUpDto, it contains previous appointment details for a follow-up appointment.
     * @param appointId this variable contains appointmentId.
     * @return It returns an FollowUpDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage>getFollowDetails(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController:: getFollowDetails");

        return appointmentService.getFollowDetails(appointId);
    }


}
