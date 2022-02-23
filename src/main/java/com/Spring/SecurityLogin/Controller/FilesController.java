package com.Spring.SecurityLogin.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class FilesController {
    @GetMapping("/login")
    public String auth(){
        return "login.html";
    }
}
