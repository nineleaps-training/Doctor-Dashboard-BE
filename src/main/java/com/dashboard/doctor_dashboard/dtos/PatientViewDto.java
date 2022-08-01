package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientViewDto {

    private Long appointId;

    private LocalTime appointmentTime;

    private String patientName;

    private String patientEmail;

    private String status;


}
