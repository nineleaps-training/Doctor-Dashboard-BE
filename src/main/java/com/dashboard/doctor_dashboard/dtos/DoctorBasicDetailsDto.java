package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class DoctorBasicDetailsDto {
    private String firstName;
    private String email;
    private Category speciality;
    private String phoneNo;
    private Gender gender;
    private Short age;
    private String degree;
    private short exp;


}
