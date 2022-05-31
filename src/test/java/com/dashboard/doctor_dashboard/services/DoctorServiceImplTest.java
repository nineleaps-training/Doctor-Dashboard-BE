package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorServiceImpl;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


//    @Test
//    void testAddDoctor() {
//        DoctorDetails newDoctor = new DoctorDetails();
//        newDoctor.setId(1L);
//        newDoctor.setFirstName("Sagar");
//        newDoctor.setLastName("Negi");
//        newDoctor.setEmail("sagarssn23@gmail.com");
//        DoctorDetails doctorDetails = new DoctorDetails(1L,"Sagar","Singh", (short) 21,
//                "sagarssn23@gmail.com","orthology",
//                null,"male",null,null);
//
//        Mockito.doReturn(doctorDetails).when(doctorRepository).save(Mockito.any(DoctorDetails.class));
//
//        DoctorDetails newDoctorDetails = doctorService.addDoctor(doctorDetails);
//
//        assertThat(newDoctorDetails).isNotNull();
//        verify(doctorRepository).save(Mockito.any(DoctorDetails.class));
//    }

    @Test
    void testGetAllDoctors() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","orthology");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","orthology");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.getAllDoctors(id)).thenReturn(list);

        ResponseEntity<GenericMessage> newList = doctorService.getAllDoctors(id);

        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
        assertEquals(Constants.SUCCESS,newList.getBody().getStatus());
    }

    @Test
    void throwErrorIfIdNotPresentInDb() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","orthology");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","orthology");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.getAllDoctors(id);
        });
    }


    @Test
    void getDoctorById() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.findDoctorById(id)).thenReturn(doctorDetails);

        ResponseEntity<GenericMessage> newDetails = doctorService.getDoctorById(id);

        assertThat(newDetails).isNotNull();
        assertEquals(doctorDetails,newDetails.getBody().getData());
    }

    @Test
    void throwErrorWhenIdNoPresent() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        System.out.println(doctorService.getDoctorById(id));

        assertEquals(null,doctorService.getDoctorById(id));
        verify(doctorRepository,never()).findDoctorById(id);
    }


    @Test
    void addDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(null);
        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getId(),request);
        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getId(),request);

        verify(doctorRepository,times(2))
                .insertARowIntoTheTable(
                        doctorFormDto.getId(),
                        doctorFormDto.getAge(),
                        doctorFormDto.getSpeciality(),
                        doctorFormDto.getPhoneNo(),
                        doctorFormDto.getGender(),
                        1L
                );

    }

    @Test
    void IfIdMisMatchForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Long id = 1L;
        Long id1 = 1L;
        Long id2 = 2L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(id1,(short) 21,"orthology","male",
                null);

        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);
        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(id);


        Mockito.when(doctorRepository.isIdAvailable(id2))
                .thenReturn(id2);

        assertThrows(APIException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });
    }

    @Test
    void ThrowErrorIfIdNotPresentForAddDoctorInDatabase() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

        verify(doctorRepository,never()).updateDoctorDb(
                doctorFormDto.getAge(),
                doctorFormDto.getSpeciality(),
                doctorFormDto.getGender(),
                doctorFormDto.getPhoneNo(),
                doctorFormDto.getId()
        );
    }



    @Test
    void updateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());
        doctorService.updateDoctor(doctorFormDto,doctorFormDto.getId(),request);
        doctorService.updateDoctor(doctorFormDto,doctorFormDto.getId(),request);

        verify(doctorRepository,times(2))
                .updateDoctorDb(
                        doctorFormDto.getAge(),
                        doctorFormDto.getSpeciality(),
                        doctorFormDto.getGender(),
                        doctorFormDto.getPhoneNo(),
                        doctorFormDto.getId()
                        );

    }

    @Test
    void IfIdMisMatchForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Long id = 1L;
        Long id1 = 1L;
        Long id2 = 2L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(id1,(short) 21,"orthology","male",
                null);

        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);
        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(id);


        Mockito.when(doctorRepository.isIdAvailable(id2))
                .thenReturn(id2);

       ResponseEntity<GenericMessage> newDetails = doctorService.updateDoctor(doctorFormDto,id2,request);

       assertEquals(null,newDetails);
        verify(doctorRepository,never()).updateDoctorDb(
                doctorFormDto.getAge(),
                doctorFormDto.getSpeciality(),
                doctorFormDto.getGender(),
                doctorFormDto.getPhoneNo(),
                doctorFormDto.getId()
        );
    }

    @Test
    void ThrowErrorIfIdNotPresentForUpdateDoctorInDatabase() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

        verify(doctorRepository,never()).updateDoctorDb(
                doctorFormDto.getAge(),
                doctorFormDto.getSpeciality(),
                doctorFormDto.getGender(),
                doctorFormDto.getPhoneNo(),
                doctorFormDto.getId()
        );
    }

    @Test
    void testDeleteDoctor() {
        final Long id = 1L;

        doctorService.deleteDoctor(id);

        doctorService.deleteDoctor(id);

        verify(doctorRepository,times(2)).deleteById(id);
    }
}