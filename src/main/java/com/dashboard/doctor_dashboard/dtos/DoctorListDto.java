package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorListDto {
    private long id;
    private String name;
    private String email;
    private String profilePic;
    private String speciality;
    private short exp;
    private String degree;



}
