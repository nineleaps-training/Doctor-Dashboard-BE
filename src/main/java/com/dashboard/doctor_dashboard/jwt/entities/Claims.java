package com.dashboard.doctor_dashboard.jwt.entities;


import lombok.NoArgsConstructor;

import java.util.Objects;


@NoArgsConstructor
public class Claims {
    private Long doctorId;
    private String doctorName;
    private String doctorEmail;
    private String role;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
