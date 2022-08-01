package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDto {

    private String category;
    private String doctorName;
    private String status;
    private String patientName;
    private String patientEmail;
    private int age;
    private String gender;


}
