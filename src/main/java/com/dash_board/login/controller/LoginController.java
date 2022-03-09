package com.dash_board.login.controller;

import com.dash_board.login.Entity.Id_Token;
import com.dash_board.login.Service.LoginService;
import com.dash_board.login.Service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class LoginController {

    @Autowired
    LoginService login;
    @GetMapping("/")
    public String  hello(){
        return "hello";
    }

    @PostMapping("api/doctor")
    public String loginIdToken(@RequestBody Id_Token idToken) throws GeneralSecurityException, IOException {

        login.tokenVerification(idToken.getIdtoken());
        return "done";
    }
}
