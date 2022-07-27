package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientViewDto {

    private Long appointId;

    private LocalTime appointmentTime;

    private String patientName;

    private String patientEmail;

    private String status;

}
