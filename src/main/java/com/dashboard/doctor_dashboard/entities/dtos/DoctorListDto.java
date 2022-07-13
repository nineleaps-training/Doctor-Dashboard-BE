package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorListDto {
    private long id;
    private String name;
    private String email;
    private String profilePic;
    private String speciality;
    private short exp;
    private String degree;


}
