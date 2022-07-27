package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor.DoctorServiceImpl;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private LoginRepo loginRepo;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @InjectMocks
    private DoctorServiceImpl doctorService;



    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void testGetAllDoctors() {
        final Long id = 1L;
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<>(Arrays.asList(doctorListDto1, doctorListDto2));


        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(doctorRepository.getAllDoctors(Mockito.any(Long.class))).thenReturn(list);

        ResponseEntity<GenericMessage> newList = doctorService.getAllDoctors(id);

        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
        assertEquals(Constants.SUCCESS,newList.getBody().getStatus());
    }

    @Test
    void throwErrorIfIdNotPresentInDb() {
        final Long id = 1L;
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<>(Arrays.asList(doctorListDto1, doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.getAllDoctors(id);
        });
    }


    @Test
    void getDoctorById() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(doctorRepository.findDoctorById(Mockito.any(Long.class))).thenReturn(doctorDetails);

        ResponseEntity<GenericMessage> newDetails = doctorService.getDoctorById(id);

        assertThat(newDetails).isNotNull();
        assertEquals(doctorDetails,newDetails.getBody().getData());
    }

    @Test
    void throwErrorWhenIdNoPresent() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.getDoctorById(id);
        });
    }


    @Test
    void addDoctorTest_SUCCESS() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long tokenId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(tokenId);
        Mockito.when(loginRepo.isIdAvailable(tokenId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getLoginId())).thenReturn(null);

        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getLoginId(),request);
        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getLoginId(),request);

        verify(doctorRepository,times(2))
                .insertARowIntoTheTable(
                        doctorFormDto.getLoginId(),
                        doctorFormDto.getAge(),
                        doctorFormDto.getSpeciality(),
                        doctorFormDto.getPhoneNo(),
                        doctorFormDto.getGender(),
                        1L,
                        doctorFormDto.getExp(),
                        doctorFormDto.getDegree()
                );

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheIdProvidedForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(4L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getLoginId()))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }
    @Test
    void throwErrorIfDetailsIdMisMatchForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 3L;
        Long doctorLoginId = 2L;
        Long loginId = 3L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(6L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getLoginId()))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheDoctorLoginIdForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 3L;
        Long doctorLoginId = 6L;
        Long loginId = 3L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(3L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getLoginId()))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }

    @Test
    void ThrowErrorIfIdNotPresentInDoctorDetailsForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;
        Long id2 = 2L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(id);

        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(id2);


        assertThrows(APIException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });
    }


    @Test
    void ThrowErrorIfIdNotPresentInLoginDetailsForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });
    }



    @Test
    void updateDoctorTest_SUCCESS() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L, "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());
        doctorService.updateDoctor(doctorFormDto,id,request);
        doctorService.updateDoctor(doctorFormDto,id,request);

        verify(doctorRepository,times(2))
                .updateDoctorDb(doctorFormDto.getMobileNo());

    }





    @Test
    void throwErrorDetailsNotEqualsWithTheIdProvidedForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(4L,
                "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }
    @Test
    void throwErrorIfDetailsIdMisMatchForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 3L;
        Long doctorLoginId = 4L;
        Long loginId = 3L;
        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(5L, "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheDoctorLoginIdForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 6L;
        Long doctorLoginId = 4L;
        Long loginId = 6L;
        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(6L,
                "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }

    @Test
    void ThrowErrorIfIdNotPresentInLoginDetailsForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L, "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }

    @Test
    void ThrowErrorIfIdNotPresentInDoctorDetailsForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L,
                "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class)))
                .thenReturn(id);
        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }

    @Test
    void ThrowErrorIfIdNotPresentInDatabaseForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        UserDetailsUpdateDto doctorFormDto = new UserDetailsUpdateDto(1L,
                "9728330045");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);
        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }





    @Test
    void testDeleteDoctor() {
        final Long id = 1L;

        doctorService.deleteDoctor(id);
        doctorService.deleteDoctor(id);

        verify(doctorRepository,times(2)).deleteById(id);
    }

    @Test
    void getAllDoctorsBySpeciality() {
        final String speciality = "orthologist";
        int pageNo=0;
        int pageSize=10;

        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthologist",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthologist",(short)6,"MBBS");

        Page<DoctorListDto> list = new PageImpl<>(List.of(doctorListDto1,doctorListDto2));

        Mockito.when(doctorRepository.isSpecialityAvailable(speciality)).thenReturn(speciality);
        Mockito.when(doctorRepository.getAllDoctorsBySpeciality(speciality,PageRequest.of(pageNo,pageSize))).thenReturn(list);

        ResponseEntity<GenericMessage> newList = doctorService.getAllDoctorsBySpeciality(speciality,pageNo,pageSize);

        assertThat(newList).isNotNull();
        System.out.println("expected"+list+" , outcome"+newList.getBody().getData());
        assertEquals(new PageRecords(list.toList(),pageNo,pageSize,list.getTotalElements(),list.getTotalPages(),list.isLast()),newList.getBody().getData());
    }

    @Test
    void throwErrorNoDoctorIsPresentBySpeciality() {
        final String speciality = "orthologist";
        int pageNo=0;
        int pageSize=10;
        Mockito.when(doctorRepository.isSpecialityAvailable(speciality)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.getAllDoctorsBySpeciality(speciality,pageNo,pageSize);
        });
    }

    @Test
    void genderChart() {
        final Long id = 1L;
        List<String> chart = new ArrayList<>(Arrays.asList("Male","female","female"));
        Map<String,Integer> m = new HashMap<>();
        m.put("Male",1);
        m.put("female",2);


        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.genderChart(Mockito.any(Long.class))).thenReturn(chart);

        ResponseEntity<GenericMessage> newList = doctorService.genderChart(id);
        assertThat(newList).isNotNull();
        assertEquals(m,newList.getBody().getData());
    }


    @Test
    void throwErrorIfIdNotPresentForGenderChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.genderChart(id);
        });

    }


    @Test
    void bloodGroupChart() {
        final Long id = 1L;
        List<String> bloodChart = new ArrayList<>(Arrays.asList("A+","B+","A+"));
        Map<String,Integer> m = new HashMap<>();
        m.put("A+",2);
        m.put("B+",1);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.bloodGroupChart(Mockito.any(Long.class))).thenReturn(bloodChart);

        ResponseEntity<GenericMessage> newList = doctorService.bloodGroupChart(id);

        assertThat(newList).isNotNull();
        assertEquals(m,newList.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentForBloodGroupChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()-> doctorService.bloodGroupChart(id));
    }

    @Test
    void ageGroupChart() {
        final Long id = 1L;
        List<Long> ageChart = new ArrayList<>(Arrays.asList(2L,24L,35L,64L,12L,78L));
        Map<String,Integer> chart = new HashMap<>();
        var week1 = "0-2";
        var week2 = "3-14" ;
        var week3 = "15-25";
        var week4 = "26-64";
        var week5 = "65+";

        chart.put(week1,1);
        chart.put(week2,1);
        chart.put(week3,1);
        chart.put(week4,2);
        chart.put(week5,1);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.ageGroupChart(Mockito.any(Long.class))).thenReturn(ageChart);

        ResponseEntity<GenericMessage> newAgeChart = doctorService.ageGroupChart(id);
        assertThat(newAgeChart).isNotNull();
        assertEquals(chart,newAgeChart.getBody().getData());

    }


    @Test
    void throwErrorIfIdNotPresentForAgeGroupChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()-> doctorService.ageGroupChart(id)
        );
    }


}