package com.sagar.Dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorClaims {
    public Long doctorId;
    public String doctorName;
    public String doctorEmail;
}
