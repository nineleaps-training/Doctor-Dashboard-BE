package com.dashboard.doctor_dashboard.services;


import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.login_entity.DoctorLoginDetails;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.services.login_service.LoginServiceImpl;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginServiceTest {

    @Mock
    LoginRepo loginRepo;

    @InjectMocks @Spy
    LoginServiceImpl loginService;

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
    void addingNewUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("given_name","pranay");
        docDetails.put("family_name","Reddy");
        docDetails.put("hd","nineleaps");
        DoctorLoginDetails doctorLoginDetails=new DoctorLoginDetails(1L,"Pranay","Reddy","pranay@gmail.com","nineleaps",null);

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f=loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void CheckingExistingUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("given_name","pranay");
        docDetails.put("family_name","Reddy");
        docDetails.put("hd","nineleaps");
        DoctorLoginDetails doctorLoginDetails=new DoctorLoginDetails(1L,"Pranay","Reddy","pranay@gmail.com","nineleaps",null);

        Mockito.when(loginRepo.findByEmailId(doctorLoginDetails.getEmailId())).thenReturn(doctorLoginDetails);

        Boolean f=loginService.addUser(docDetails);
        assertEquals(false,f);
    }

    @Mock
    DoctorService doctorService;
    @Test
    void newUserInfoFromGoogleToken(){
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        byte[] b = new byte[5];
//        DoctorDetails newDoctor = new DoctorDetails();
//        newDoctor.setId(1L);
//        newDoctor.setFirstName("pranay");
//        newDoctor.setLastName("Reddy");
//        newDoctor.setEmail("pranay@gmail.com");
        Mockito.doReturn(true).when(loginService).addUser(Mockito.any());
        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).loginCreator(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));
        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        String expected=loginService.takingInfoFromToken(idToken);
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",expected);
    }
    @Test
    void existingUserInfoFromGoogleToken(){
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        byte[] b = new byte[5];
        DoctorDetails newDoctor = new DoctorDetails();
//        newDoctor.setId(1L);
//        newDoctor.setFirstName("pranay");
//        newDoctor.setLastName("Reddy");
//        newDoctor.setEmail("pranay@gmail.com");
        Mockito.doReturn(false).when(loginService).addUser(Mockito.any());
        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).loginCreator(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        String value=loginService.takingInfoFromToken(idToken);
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",value);
    }
    @Test
    void InvalidGoogleToken(){
        String value=loginService.takingInfoFromToken(null);
        assertEquals("ID token expired.",value);
    }


    @Mock
    JwtServiceImpl jwtService;
    @Test
    void loginCreator(){
        Login login =new Login(1L,"pranay","pranay@gmail.com");
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(jwtService).authenticateUser(Mockito.any());

        String actual=loginService.loginCreator(1L,"pranay@gmail.com","pranay");
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",actual);
    }
}
