package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.util.wrappers.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import io.swagger.annotations.ApiOperation;
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
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * This endpoint is used to book an appointment.
     * @param appointment this object contains appointment details.
     * @param request this object contains information related to user HTTP request.
     * @return A success message is returned wrapped under ResponseEntity<GenericMessage> with HTTP status code 201.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @ApiOperation("Saves the details in appointment table")
    @PostMapping("/patient")
    public ResponseEntity<GenericMessage> addAppointment(@Valid  @RequestBody AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        log.info("AppointmentController:: addAppointment");
        return appointmentService.addAppointment(appointment,request);

    }

    /**
     *  This endpoint returns a List<Boolean> with available time slots of the doctor on a given date.
     * @param date this variable contains date.
     * @param loginId this variable contains loginId
     * @return It returns a List<Boolean> wrapped under <GenericMessages> with HTTP status 200.
     */
    @ApiOperation("Shows all the available slots of the doctor")
    @GetMapping("/getAvailableSlots/{loginId}/{date}")
    public ResponseEntity<GenericMessage> showAvailableSlots(@PathVariable("date") String  date,@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: showAvailableSlots");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentService.checkSlots(LocalDate.parse(date),loginId)),HttpStatus.OK);
    }

    /**
     *  This endpoint creates a Map<String,List<PatientAppointmentListDto>with values post , today and upcoming of the doctor based on patientId.
     * @param pageNo this variable contains pageNo.
     * @return It returns a List<PatientAppointmentListDto> with values post , today and upcoming,  wrapped under <GenericMessages> with HTTP status 200.
     */
    @ApiOperation("Shows all appointments of the patient")
    @GetMapping("/getAllAppointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId,@RequestParam("pageNo") int pageNo) {
        log.info("AppointmentController:: getAllAppointmentByPatientId");
        return appointmentService.getAllAppointmentByPatientId(patientId,pageNo) ;
    }

    /**
     *  This endpoint creates a Map<String,List<PatientAppointmentListDto>with values post , today and upcoming of the doctor based on loginId.
     * @param doctorId this variable contains doctorId.
     * @param pageNo this variable contains pageNo.
     *  It returns a List<PatientAppointmentListDto> with values post , today and upcoming,  wrapped under <GenericMessages> with HTTP status 200.
     */
    @ApiOperation("Shows all appointments of the doctor")
    @GetMapping("/getAllAppointments/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId,@RequestParam("pageNo") int pageNo) {
        log.info("AppointmentController::getAllAppointmentByDoctorId");
        return appointmentService.getAllAppointmentByDoctorId(doctorId, pageNo) ;
    }


    /**
     *  This endpoint creates a PatientprofileDto based on appointmentId for patient to view an appointment,
     * @param appointId this variable contains appointmentId.
     * @return It creates a patientProfileDto wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @ApiOperation("appointment details of patient for the doctor")
    @GetMapping("/{appointId}/patient")
    public ResponseEntity<GenericMessage> getAppointmentById(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController::getAppointmentById");

        return appointmentService.getAppointmentById(appointId);
    }

    /**
     * This endpoint creates an Array<String> based on doctorId for doctor to view weekly appointments count in a month.
     * @param loginId this variable contains loginId.
     * @return It returns an Array<String> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @ApiOperation("return no of appointment booked for the doctor per week in a month")
    @GetMapping("chart/{loginId}/weeklyGraphDoctor")
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: weeklyDoctorCountChart");

        return appointmentService.weeklyDoctorCountChart(loginId);
    }

    /**
     * This endpoint creates an Array<String> based on patientId for doctor to view weekly appointments booked by patients in a month.
     * @param patientId this variable contains patientId.
     * @return It returns an Array<String> wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @ApiOperation("return no of appointment booked by patient per week in a month")
    @GetMapping("chart/{patientId}/weeklyGraphPatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        log.info("AppointmentController::weeklyPatientCountChart");
        return appointmentService.weeklyPatientCountChart(patientId);
    }


    /**
     *  This endpoint creates an List<AppointmentListDto> it contains upcoming appointments, for the doctor
     * @param loginId loginId this variable contains loginId.
     * @return  It returns an List<AppointmentListDto>  wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     *
     */
    @ApiOperation("return today's latest 3 recent appointment booked for the doctor")
    @GetMapping("/recentAdded/doctor/{loginId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: recentAppointment");

        return appointmentService.recentAppointment(loginId);
    }



    /**
     * This endpoint creates a Array<String>based on patient , to view an appointments booked by a patient in a month.
     * @param loginId loginId this variable contains loginId.
     * @return  It returns int wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @ApiOperation("return total no of appointments for the doctor")
    @GetMapping("/chart/{loginId}/totalPatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointment");
        return appointmentService.totalNoOfAppointment(loginId);
    }

    /**
     *  This endpoint returns on int of total number of Appointments for current day  ,of the doctor.
     *@param loginId loginId this variable contains loginId.
     * @return It returns int  wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
z     */
    @ApiOperation("return today's appointment count for the doctor")
    @GetMapping("/chart/{loginId}/todayAppointments")
    public ResponseEntity<GenericMessage> todayAppointments(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: todayAppointments");

        return appointmentService.todayAppointments(loginId);
    }

    /**
     *  This endpoint returns on int of total number of Appointments for current week ,of the doctor.
     *@param loginId loginId this variable contains loginId.
     * @return This endpoint returns on int of total number of Appointments for current week ,of the doctor.
     */
    @ApiOperation("returns total no of appointment booked in a week for the doctor")
    @GetMapping("/chart/{loginId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController:: totalNoOfAppointmentAddedThisWeek");

        return appointmentService.totalNoOfAppointmentAddedThisWeek(loginId);

    }

    /**
     *  This endpoint returns on Array<String> of categories and their count, which are appointments booked by patient with different category doctors.
     * @param loginId this variable contains loginId.
     * @returnthis  It returns int  wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     *
     */
    @ApiOperation("returns categories of the appointment booked and it's count according to the id provided ")
    @GetMapping("/{loginId}/category")
    public ResponseEntity<GenericMessage> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController::patientCategoryGraph");
        return appointmentService.patientCategoryGraph(loginId);
    }

    /**
     *  This endpoint returns on FollowUpDto , it contains previous appointment details for a follow-up appointment.
     * @param appointId this variable contains AppointmentId.
     * @return  It returns int  wrapped under ResponseEntity<GenericMessage> with HTTP status code 200.
     */
    @ApiOperation("returns detail of doctor for which patient is booking appointment again")
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage>getFollowDetails(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController:: getFollowDetails");

        return appointmentService.getFollowDetails(appointId);
    }


}
