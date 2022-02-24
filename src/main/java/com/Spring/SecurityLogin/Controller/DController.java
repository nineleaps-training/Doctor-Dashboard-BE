package com.Spring.SecurityLogin.Controller;

import com.Spring.SecurityLogin.Security.OAuth.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Map;

@RestController
public class DController {
//    @Autowired
    CustomOAuth2User customOAuth2User;
    @GetMapping("/user")
    public String user(){

        return "login Successfull!";
    }
    @GetMapping("/check")
    public String check(){
        return "worked!!";
    }
//
//    @GetMapping("/Admin")
//    public String admin(){
//        return "Hey Admin!!";
//    }
//
//    @GetMapping("/test")
//    public String test(){
//        return "Test String";
//    }
}
