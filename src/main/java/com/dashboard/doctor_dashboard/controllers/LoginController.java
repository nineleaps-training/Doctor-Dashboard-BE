package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.JwtToken;
import com.dashboard.doctor_dashboard.services.LoginService;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class LoginController {

    private final LoginService loginService;
    @Autowired

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
    /**
     * @param idToken contains google id token for Authenticating
     * @return Jwt token which will be used to access all the API
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws JSONException
     * This endpoint is used for logging into the application.
     */
    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericMessage> tokenAuthentication(@Valid @RequestBody JwtToken idToken) throws GeneralSecurityException, IOException, JSONException {
        //authToken
        log.info("LoginController:: tokenAuthentication");
        return loginService.tokenVerification(idToken.getIdtoken());
    }

    @GetMapping(value = "/user/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericMessage> refreshTokenAuthentication(HttpServletRequest request) throws GeneralSecurityException, IOException, JSONException {
        //authToken
        log.info("refreshTokenAuthentication:: tokenAuthentication");
        return loginService.refreshTokenCreator(request);
    }
    /**
     * @return status 200 ok if the server is up and running.
     * This endpoint returns the status of  API.
     */

    @GetMapping(value = "/check")
    public ResponseEntity<String> checkServerStatus(){
        log.info("LoginController:: checkServerStatus");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param id is used as path variable
     * @return Successfully deleted message after deleting user details.
     */
    @DeleteMapping(value = "private/doctor/login/{id}")
    public String deleteUserById(@PathVariable("id") long id ){
        log.info("LoginController:: deleteDoctorById");
        return loginService.deleteUserById(id);
    }
}