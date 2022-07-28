package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.exceptions.CommonExceptionHandler;
import com.dashboard.doctor_dashboard.services.DoctorService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoctorControllerTest {


    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;


    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).setControllerAdvice(new CommonExceptionHandler()).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }



    @Test
    void getDoctorsByIdIfIdPresent() throws Exception {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorDetails);


        Mockito.when(doctorService.getDoctorById(id)).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/id/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void addDoctorDetails() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"Orthologist","male",
                "9728330045",(short)6,"MBBS");
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.CREATED));


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/doctor/add-doctor-details/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }
    @Autowired
    LocalValidatorFactoryBean validator;
    @Test
    void checkIfAddDoctorDetailsHasError() throws Exception {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

//        ObjectError error = new ObjectError("age","age should be between 24-100");
//        result.addError(error);
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/doctor/add-doctor-details/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isUnprocessableEntity());


    }

    @Test
    void updateDoctorDetails() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L, "9728330045");

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.updateDoctor(Mockito.any(UserDetailsUpdateDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/doctor/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());

    }

    @Test
    void checkIfUpdateDoctorDetailsHasError() throws Exception {

        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L,
                "99999999999999999999999");


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/doctor/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isUnprocessableEntity());


    }

    @Test
    void deleteDoctor() throws Exception {
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(doctorService.deleteDoctor(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/doctor/private/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getAllDoctorsBySpecialityTest() throws Exception {
        final String speciality = "orthology";
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<DoctorListDto>(Arrays.asList(doctorListDto1, doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctorsBySpeciality(Mockito.any(String.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/all-doctors/orthology?pageNo=0&pageSize=2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void genderChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("Male",2);
        m.put("Female",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.genderChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/gender").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }

    @Test
    void bloodGroupChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("A+",2);
        m.put("AB-",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.bloodGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/blood-group").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void ageGroupChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("0-2",1);
        m.put("3-14",2);
        m.put("26-64",7);


        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.ageGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/age-group").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }
}