package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DoctorDropdownDto {

    private long id;
    private String name;
    private String emailId;
    private String speciality;

}
