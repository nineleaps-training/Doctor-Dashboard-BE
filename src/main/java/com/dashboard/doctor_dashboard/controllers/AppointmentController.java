package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.services.AppointmentService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("api/v1/appointment")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;


    @Autowired
    public AppointmentController(AppointmentService appointmentService){

        this.appointmentService = appointmentService;
    }

    /**
     * This endpoint is used to book an appointment
     *
     * @param appointment this object contains appointment details.
     * @param request     this object contains information related to user HTTP request.
     * @return A success message is returned wrapped under ResponseEntity<Map<String, String>>with HTTP status code 201.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/patient")
    public ResponseEntity<Map<String, String>> addAppointment(@Valid  @RequestBody AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        log.info("AppointmentController:: addAppointment");
        return new ResponseEntity<>( appointmentService.addAppointment(appointment,request),HttpStatus.CREATED);

    }

    /**
     * This endpoint creates a List<Boolean> with available time slots of the doctor on a given date.

     * @param date this variable contains date.
     * @param loginId this variable contains loginId.
     * @return It returns a List<Boolean> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/available-slots/{loginId}/{date}")
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
    @GetMapping("/all-appointments/patient/{patientId}")
    public ResponseEntity<Map<String, PageRecords>> allAppointmentByPatientId(@PathVariable("patientId") Long patientId, @RequestParam("pageNo") int pageNo, @RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("AppointmentController:: allAppointmentByPatientId");
        return new ResponseEntity<>(appointmentService.getAllAppointmentByPatientId(patientId,pageNo,pageSize), HttpStatus.OK);
    }

    /**
     * This endpoint creates a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming of the doctor based on loginId.
     * @param loginId this variable contains loginId.
     * @param pageNo this variable contains pageNo.
     * @return It returns a Map<String,List<PatientAppointmentListDto>> with values past,today,upcoming wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @GetMapping("/all-appointments/doctor/{loginId}")
    public ResponseEntity<Map<String, PageRecords>> allAppointmentByDoctorId(@PathVariable("loginId") Long loginId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("AppointmentController:: allAppointmentByDoctorId");
        return new ResponseEntity<>(appointmentService.getAllAppointmentByDoctorId(loginId, pageNo,pageSize ),HttpStatus.OK);
    }


    /**
     * This endpoint creates a PatientProfileDto based on appointmentId for patient to view an appointment.
     * @param appointId this variable contains appointmentId.
     * @return It returns a PatientProfileDto wrapped under ResponseEntity<PatientProfileDto> with HTTP status code 200.
     */
    @GetMapping("/{appointId}/patient")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PatientProfileDto> appointmentById(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController:: appointmentById");

        return new ResponseEntity<>(appointmentService.getAppointmentById(appointId),HttpStatus.OK) ;
    }

    /**
     * This endpoint creates a ArrayList<String> based on loginId for doctor to view weekly appointments count in a month.

     * @param loginId this variable contains loginId.
     * @return It returns a ArrayList<String> wrapped under ResponseEntity<ArrayList<String>> with HTTP status code 200.
     */
    @GetMapping("chart/{loginId}/weekly-graph-doctor")
    public ResponseEntity<ArrayList<String>> weeklyDoctorCountChart(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: weeklyDoctorCountChart");

        return new ResponseEntity<>( appointmentService.weeklyDoctorCountChart(loginId),HttpStatus.OK);
    }

    /**
     * This endpoint creates a ArrayList<String> based on patientId, to view weekly appointments booked by a patient in a month.

     * @param patientId this variable contains patientId.
     * @return It returns a ArrayList<String> wrapped under ResponseEntity<ArrayList<String>> with HTTP status code 200.
     */
    @GetMapping("chart/{patientId}/weekly-graph-patient")
    public ResponseEntity<ArrayList<String>> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        log.info("AppointmentController:: weeklyPatientCountChart");

        return new ResponseEntity<>(appointmentService.weeklyPatientCountChart(patientId),HttpStatus.OK);
    }


    /**
     * This endpoint creates a List<DoctorAppointmentListDto> it contains upcoming appointments of the doctor of the current day with limit 3.

     * @param loginId loginId this variable contains loginId.
     * @return It returns a List<DoctorAppointmentListDto> wrapped under ResponseEntity<List<DoctorAppointmentListDto>> with HTTP status code 200.
     */
    @GetMapping("/recent-added/doctor/{loginId}")
    public ResponseEntity<List<DoctorAppointmentListDto>> recentAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: recentAppointment");

        return new ResponseEntity<>(appointmentService.recentAppointment(loginId),HttpStatus.OK);
    }


    /**
     * This endpoint returns an int of total number of appointments of the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<Integer> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/total-patient")
    public ResponseEntity<Integer> totalNoOfAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointment");

        return new ResponseEntity<>(appointmentService.totalNoOfAppointment(loginId),HttpStatus.OK);
    }

    /**
     * This endpoint returns an int of total number of appointments for current day, for the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<Integer> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/today-appointments")
    public ResponseEntity<Integer> todayAppointments(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: todayAppointments");

        return new ResponseEntity<>( appointmentService.todayAppointments(loginId),HttpStatus.OK);
    }

    /**
     * This endpoint returns an int of total number of appointments added in the current week, for the doctor.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<Integer>> with HTTP status code 200.
     */
    @GetMapping("/chart/{loginId}/appointments-this-week")
    public ResponseEntity<Integer> appointmentsAddedThisWeek(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointmentAddedThisWeek");

        return new ResponseEntity<>(appointmentService.totalNoOfAppointmentAddedThisWeek(loginId),HttpStatus.OK);
    }

    /**
     * This endpoint returns an ArrayList<String> of categories and their count, which are appointments booked by patient with different category doctors.
     * @param loginId loginId this variable contains loginId.
     * @return It returns an int wrapped under ResponseEntity<ArrayList<String>> with HTTP status code 200.
     */
    @GetMapping("/{loginId}/category")
    public ResponseEntity<ArrayList<String>> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: patientCategoryGraph");

        return new ResponseEntity<>(appointmentService.patientCategoryGraph(loginId),HttpStatus.OK);
    }

    /**
     * This endpoint returns an FollowUpDto, it contains previous appointment details for a follow-up appointment.
     * @param appointId this variable contains appointmentId.
     * @return It returns an FollowUpDto wrapped under ResponseEntity<FollowUpDto> with HTTP status code 200.
     */
    @GetMapping("/{appointId}")
    public ResponseEntity<FollowUpDto>getFollowDetails(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController:: getFollowDetails");

        return new ResponseEntity<>(appointmentService.getFollowDetails(appointId),HttpStatus.OK);
    }


}