package com.dashboard.doctor_dashboard.controllers;


import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class LoginController {

    private LoginService loginService;
    @Autowired

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericMessage> tokenAuthentication(@RequestBody JwtToken idToken) throws GeneralSecurityException, IOException, JSONException {
        //authToken
        var jwt = new JwtToken();
        jwt.setIdtoken(loginService.tokenVerification(idToken.getIdtoken()));
        var jsonObject = new JSONObject();
        if (!jwt.getIdtoken().equals("ID token expired.")) {
            jsonObject.put("jwt_token", jwt.getIdtoken());
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,jsonObject.toMap()) , HttpStatus.CREATED);
        }
        throw new GoogleLoginException(jwt.getIdtoken());
    }

    @GetMapping(value = "/check")
    public ResponseEntity<String> checkServerStatus(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value = "/doctor/login/delete/{id}")
    public String deleteDoctorById(@PathVariable("id") long id ){
        return loginService.deleteDoctorById(id);
    }
}
