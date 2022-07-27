package com.dashboard.doctor_dashboard.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JwtToken {

    @NotNull
    @NotEmpty
    private String token;

    public String getIdtoken() {
        return token;
    }

    public void setIdtoken(String token) {
        this.token = token;
    }
}
