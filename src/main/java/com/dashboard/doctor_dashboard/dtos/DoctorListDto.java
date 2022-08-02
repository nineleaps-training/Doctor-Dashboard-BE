package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
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
    private Category speciality;
    private short exp;
    private String degree;


}
