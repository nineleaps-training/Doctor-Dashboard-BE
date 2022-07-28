package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtServiceImpl;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.impl.LoginServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    LoginRepo loginRepo;

    @Mock
    JwtServiceImpl jwtService;

    @Mock
    DoctorService doctorService;

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
    void addingNewUser_SUCCESS(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd","nineleaps.com");
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void checkIfTheDomainIsNotNineleapsForAddUser(){
        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","sagar");
        docDetails.put("hd","gmail.com");
        docDetails.put("email","sagar@gmail.com");
        docDetails.put("picture","picture2");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void checkIfTheDomainIsNullForAddUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","gokul");
        docDetails.put("hd",null);
        docDetails.put("email","gokul@gmail.com");
        docDetails.put("picture","picture2");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void CheckingExistingUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd","nineleaps.com");
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");
        LoginDetails loginDetails=new LoginDetails(1L,"Pranay","pranay@gmail.com","nineleaps","profilePic1","doctor",false,null,null,null);

        Mockito.when(loginRepo.findByEmailId(loginDetails.getEmailId())).thenReturn(loginDetails);

        Boolean f=loginService.addUser(docDetails);
        assertEquals(false,f);
    }


    @Test
    void newUserInfoFromGoogleToken(){
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUB";
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        payload.set("picture","picture1");
        byte[] b = new byte[5];
        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        System.out.println(idToken);

        Mockito.when(jwtService.authenticateUser(Mockito.any(Login.class))).thenReturn(token);

        String expectedToken = loginService.takingInfoFromToken(idToken);
        assertEquals(token,expectedToken);
    }

    @Test
    void checkIfGoogleTokenIsNull(){
        String message = "Invalid ID token.";

        GoogleLoginException googleLoginException=assertThrows(GoogleLoginException.class,()->loginService.takingInfoFromToken(null));
        assertThat(googleLoginException).isNotNull();
        assertEquals(message,googleLoginException.getMessage());
    }


    @Test
    void tokenVerification() throws GeneralSecurityException, IOException, JSONException {
        String message = "Invalid ID token.";

        String idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYzMWZhZTliNTk0MGEyZDFmYmZmYjAwNDAzZDRjZjgwYTIxYmUwNGUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNjYyOTc4MTQ2NTktZ2tqNjhsZnUxMTZhaTE5dGI2ZTJyZmFjcXQ5YmphMHMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NjI5NzgxNDY1OS1na2o2OGxmdTExNmFpMTl0YjZlMnJmYWNxdDliamEwcy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMTEwNzgzMDg2MTcxMTUwOTU3OSIsImhkIjoibmluZWxlYXBzLmNvbSIsImVtYWlsIjoicHJhbmF5Lm5hcmVkZHlAbmluZWxlYXBzLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiZDZSOFRmdFZSMm8zWXN6OTZEY1lLUSIsIm5hbWUiOiJwcmFuYXkgbmFyZWRkeSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tcVhTdGZ4RVhObHJfT3U1ZDliU21Jd0ZfNVFzX3g5Q0ZOTFVvPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6InByYW5heSIsImZhbWlseV9uYW1lIjoibmFyZWRkeSIsImxvY2FsZSI6ImVuIiwiaWF0IjoxNjU4MjIxNzQwLCJleHAiOjE2NTgyMjUzNDAsImp0aSI6IjJjYWQxODIyM2E1NTEzZTJkYTdlODM5OTY3ZjMwOWMwMDg2NTMzMDcifQ.moCDJsA0NAcIxKvK3mWJOHZaW5gnO665LUv4ore7MEKmfhgp-kfsESk9jFYJv-kWmj2JaOmJTo6zCBZVcdlUQ96dafMlYJRkj48RIDbOVEMTtaMl5YRWPXYvJinHHN4QcIXpY27_F8w8YKFPxImNSz6Uq7DeasvAzcOlRfzrxmFWAxtdaVWawci423Ubz6GSkzb03ldhDmx_CZSepfH02CKkOOc-ytVXhsqWDIZKgUKvrEToAr254UJTf0sjGqaHzwHSxErVcAKsS-JgrIWDsSF_FnCoZNFZffcIVsVu6292-otwtaPn-tZsbatrylx8tQcMF_ezgss1n-cXKcTjqw";
//        String jwtToken="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MiwiZG9jdG9yTmFtZSI6InByYW5heSIsImRvY3RvckVtYWlsIjoicHJhbmF5Lm5hcmVkZHlAbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUl0YnZtbXFYU3RmeEVYTmxyX091NWQ5YlNtSXdGXzVRc194OUNGTkxVbz1zOTYtYyJ9LCJyb2xlIjoiRE9DVE9SIiwiZXhwIjoxNjU4MzA4NDMwLCJpYXQiOjE2NTgyMjIwMzB9.XfCkZCob1Yfe1yVl1v5Xu7aHN0UdavHs-vZkdLPt9CO6TI1BYX13anXNVsn1Gnv8qLD2yHFmX4Cu-tJrRU3tyA";
        GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
        Mockito.when(verifier.verify(Mockito.any(String.class))).thenReturn(null);


        GoogleLoginException googleLoginException=assertThrows(GoogleLoginException.class,()->loginService.tokenVerification(idTokenString));
        System.out.println("googleLoginException"+googleLoginException);
        assertThat(googleLoginException).isNotNull();
        assertEquals(message,googleLoginException.getMessage());


    }


//    @Test
//    void existingUserInfoFromGoogleToken(){
//        JsonWebSignature.Header header=new JsonWebSignature.Header();
//        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
//        payload.set("email","pranay@gmail.com");
//        payload.set("given_name","pranay");
//        payload.set("family_name","Reddy");
//        byte[] b = new byte[5];
//        DoctorDetails newDoctor = new DoctorDetails();
////        newDoctor.setId(1L);
////        newDoctor.setFirstName("pranay");
////        newDoctor.setLastName("Reddy");
////        newDoctor.setEmail("pranay@gmail.com");
//        Mockito.doReturn(false).when(loginService).addUser(Mockito.any());
//        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
//        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).
//                loginCreator(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class));
//
//        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
//        String value=loginService.takingInfoFromToken(idToken);
//        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",value);
//    }

    @Test
    void InvalidGoogleToken(){
        String message = "Invalid ID token.";
        GoogleLoginException googleLoginException=assertThrows(GoogleLoginException.class,()->loginService.takingInfoFromToken(null));
        assertThat(googleLoginException).isNotNull();
        assertEquals(message,googleLoginException.getMessage());
    }


    @Test
    void loginCreator(){
        Login login =new Login(1L,"pranay","pranay@gmail.com","PATIENT","profilePic1");
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(jwtService).authenticateUser(Mockito.any());

        String actual=loginService.loginCreator(1L,"pranay@gmail.com","pranay","PATIENT","profilePic1");
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",actual);
    }

    @Test
    void deleteDoctorById(){
        final Long doctorId = 1L;
        loginService.deleteDoctorById(doctorId);
        loginService.deleteDoctorById(doctorId);

        verify(loginRepo,times(2)).deleteById(doctorId);
    }
}