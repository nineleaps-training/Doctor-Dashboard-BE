package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorAppointmentListDto {

    private Long appointId;
    private LocalDate dateOfAppointment;
    private String patientName;
    private String patientEmail;
    private String status;
    private LocalTime appointmentTime;

}

