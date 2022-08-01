package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class DoctorBasicDetailsDto {
    private String firstName;
    private String email;
    private String speciality;
    private String phoneNo;
    private String gender;
    private Short age;
    private String degree;
    private short exp;


}
