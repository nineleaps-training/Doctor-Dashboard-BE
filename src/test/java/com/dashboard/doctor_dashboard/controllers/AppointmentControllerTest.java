package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.services.AppointmentService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    void addAppointmentTest() throws Exception {
        LocalDate localDate = LocalDate.now().plusDays(1);
        LocalTime localTime = LocalTime.of(10,30);
        Map<String,String> m = new HashMap<>();
        m.put("appointId","1L");
        m.put("message","Successfully created");

        AppointmentDto appointment = new AppointmentDto("dentist",localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,null,new Patient(),new DoctorDetails());

        Mockito.when(appointmentService.addAppointment(Mockito.any(AppointmentDto.class),Mockito.any(HttpServletRequest.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,m), HttpStatus.CREATED));

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String content = objectMapper.writeValueAsString(appointment);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/appointment/patient").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    void showAvailableSlotsTest() throws Exception {

        final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);

        Mockito.when(appointmentService.checkSlots(Mockito.any(LocalDate.class),Mockito.any(Long.class))).thenReturn(timesSlots);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/available-slots/1/"+LocalDate.now().plusDays(1).toString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void getAllAppointmentByPatientIdTest() throws Exception {
        final Long patientId = 1L;
        int pageNo = 1;
        Map<String, List<PatientAppointmentListDto>> map =new HashMap<>();
        PatientAppointmentListDto dto1 = new PatientAppointmentListDto(2L,"dentist", LocalDate.now(),LocalTime.now(),"sagar","completed",true);
        PatientAppointmentListDto dto2 = new PatientAppointmentListDto(1L,"dentist", LocalDate.now(),LocalTime.now(),"sagar","completed",true);

        List<PatientAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));
        map.put("past",dto);
        map.put("today",dto);
        map.put("upcoming",dto);

        Mockito.when(appointmentService.getAllAppointmentByPatientId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,map), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/all-appointments/patient/1?pageNo=0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }

    @Test
    void getAllAppointmentByDoctorIdTest() throws Exception {
        final Long doctorId = 1L;
        int pageNo = 1;

        Map<String, List<DoctorAppointmentListDto>> map =new HashMap<>();
        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        DoctorAppointmentListDto dto2 = new DoctorAppointmentListDto(3L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));
        map.put("past",dto);
        map.put("today",dto);
        map.put("upcoming",dto);

        Mockito.when(appointmentService.getAllAppointmentByDoctorId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,map), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/all-appointments/doctor/1?pageNo=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getTotalNoOfAppointmentTest() throws Exception {
        final Long doctorId = 1L;
        int totalNoOfAppointment = 4;

        Mockito.when(appointmentService.totalNoOfAppointment(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalNoOfAppointment), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/chart/1/total-patient").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getTodayAppointmentsTest() throws Exception {
        final Long doctorId = 1L;
        int totalAppointmentToday = 2;

        Mockito.when(appointmentService.todayAppointments(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalAppointmentToday), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/chart/1/today-appointments").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getTotalNoOfAppointmentAddedThisWeekTest() throws Exception {
        final Long doctorId = 1L;
        int totalAppointmentThisWeek = 10;

        Mockito.when(appointmentService.totalNoOfAppointmentAddedThisWeek(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalAppointmentThisWeek), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/chart/1/appointments-this-week").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getPatientCategoryGraphTest() throws Exception {
        Long patientId = 1L;
        List<String> charts = new ArrayList<>();
        charts.add("dentist,4");
        charts.add("gastrologist,5");

        Mockito.when(appointmentService.patientCategoryGraph(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,charts), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/1/category").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getFollowDetailsTest() throws Exception {
        final Long appointId = 1L;
        FollowUpDto followUpDto = new FollowUpDto(1L,"sagar",1L,"dentist","completed");

        Mockito.when(appointmentService.getFollowDetails(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,followUpDto), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void weeklyDoctorCountChart() throws Exception {
        final Long doctorId = 1L;

        List<String> chartsDoctor = new ArrayList<>();
        chartsDoctor.add("week1,1");
        chartsDoctor.add("week2,2");
        chartsDoctor.add("week3,3");
        chartsDoctor.add("week4,4");
        chartsDoctor.add("week5,5");

        Mockito.when(appointmentService.weeklyDoctorCountChart(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chartsDoctor), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/chart/1/weekly-graph-doctor").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }

    @Test
    void weeklyPatientCountChart() throws Exception {
        final Long patientId = 1L;

        List<String> chartsPatient = new ArrayList<>();
        chartsPatient.add("week1,2");
        chartsPatient.add("week2,3");
        chartsPatient.add("week3,4");
        chartsPatient.add("week4,5");
        chartsPatient.add("week5,1");

        Mockito.when(appointmentService.weeklyPatientCountChart(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chartsPatient), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/chart/1/weekly-graph-patient").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void recentAppointment() throws Exception {
        final Long doctorId = 1L;

        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        DoctorAppointmentListDto dto2 = new DoctorAppointmentListDto(3L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));

        Mockito.when(appointmentService.recentAppointment(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,dto), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/recent-added/doctor/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void getAppointmentByIdTest() throws Exception {
        final Long appointId = 1L;

        PatientProfileDto patientProfileDto = new PatientProfileDto(1L,LocalDate.now(),"sagar","sagarssn3@gmail.com",
                "fever","dentist",true,1L,null,null,null,"completed");

        Mockito.when(appointmentService.getAppointmentById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientProfileDto), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/appointment/1/patient").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }


}