package com.dashboard.doctor_dashboard.entities.login_entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JwtToken {//Auth
    private String token;
    @NotNull
    @NotEmpty
    public String getIdtoken() {
        return token;
    }

    public void setIdtoken(String token) {
        this.token = token;
    }
}
