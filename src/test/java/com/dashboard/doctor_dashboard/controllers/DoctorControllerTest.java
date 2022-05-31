package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DoctorControllerTest {


    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void  getAllDoctors() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","orthology");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","orthology");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> newList = doctorController.getAllDoctors(id);
        System.out.println(newList);

        assertEquals(list,newList.getBody().getData());
//        assertEquals(doctorListDto1.getName(),newList.get(0).getName());
//        assertEquals(doctorListDto2.getName(),newList.get(1).getName());
//        assertEquals(doctorListDto1.getSpeciality(),newList.get(0).getSpeciality());
    }

    @Test
    void throwErrorIfIdNotPresentDbForAllDoctor() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","orthology");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","orthology");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorController.getAllDoctors(id);
        });

    }

    @Test
    void getDoctorsByIdIfIdPresent() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorDetails);


        Mockito.when(doctorService.getDoctorById(id)).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> newDetails = doctorController.getDoctorById(1);
        System.out.println(newDetails);

        assertEquals(doctorDetails,newDetails.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentDb() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorService.getDoctorById(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorController.getDoctorById(id);
        });

    }

    @Test
    void addDoctorDetails() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        ResponseEntity<GenericMessage> newDoctorDetails = doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        System.out.println(newDoctorDetails);
        assertEquals(200,newDoctorDetails.getStatusCodeValue());
        assertEquals(doctorFormDto,newDoctorDetails.getBody().getData());
    }

    @Test
    void checkIfAddDoctorDetailsHasError() {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(result.hasErrors()).thenReturn(true);

        WebRequest webRequest = mock(WebRequest.class);


        assertThrows(ValidationsException.class,()->{
            doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void throwErrorIfIdMisMatchForAddDoctor() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

        assertThrows(APIException.class,()->{
            doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        });

    }



    @Test
    void updateDoctorDetails() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        ResponseEntity<GenericMessage> newDoctorDetails = doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        System.out.println(newDoctorDetails);
        assertEquals(200,newDoctorDetails.getStatusCodeValue());
        assertEquals(doctorFormDto,newDoctorDetails.getBody().getData());
    }

    @Test
    void checkIfUpdateDoctorDetailsHasError() {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(result.hasErrors()).thenReturn(true);

        WebRequest webRequest = mock(WebRequest.class);


        assertThrows(ValidationsException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void throwErrorIfIdMisMatchForUpdateDoctor() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

                assertThrows(APIException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void deleteDoctor() {
        DoctorDetails doctorDetails = new DoctorDetails(1L, (short) 21,"orthology",
                null,"male",null,null,null,1L);
        doctorDetails.toString();


        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(doctorService.deleteDoctor(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> res = doctorController.deleteDoctor(1);

        assertEquals("Deleted",res.getBody().getData());

   }
}