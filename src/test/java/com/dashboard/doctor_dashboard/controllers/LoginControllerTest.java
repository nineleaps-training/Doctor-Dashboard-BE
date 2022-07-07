package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

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
    void loginJwtToken_Success() throws GeneralSecurityException, IOException, JSONException {
        String token = "abcdefghijklmnopqrstuvwxyz";
        JwtToken idToken = new JwtToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        ResponseEntity<String> newMessages = loginController.tokenAuthentication(idToken);
        System.out.println(newMessages.getBody().getClass());
        assertThat(newMessages).isNotNull();
        assertEquals(200, newMessages.getStatusCodeValue());;
        assertEquals(true, newMessages.hasBody());

    }

//    @Test
//    void ThrowErrorIfTokenExpired_Success() throws GeneralSecurityException, IOException, JSONException {
//        String tokens = "Toked Id has expired.";
//        JwtToken idToken = new JwtToken();
//        idToken.setIdtoken(tokens);
//        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(tokens);
//
//        assertThrows(GoogleLoginException.class, ()->{
//            loginController.tokenAuthentication(idToken);
//        });
//
//    }

    @Test
    void checkServerStatus_Success() {
        ResponseEntity<String> responses = loginController.checkServerStatus();
        assertEquals(HttpStatus.OK, responses.getStatusCode());
    }

    @Test
    void deleteDoctorById_Success() {
        final Long id = 1L;

        String messages = "Deleted Successfully";

        Mockito.when(loginService.deleteDoctorById(Mockito.any(Long.class))).thenReturn(messages);

        String newMessage = loginController.deleteDoctorById(id);
        assertThat(newMessage).isNotNull();
        assertEquals(messages,newMessage);
    }


}