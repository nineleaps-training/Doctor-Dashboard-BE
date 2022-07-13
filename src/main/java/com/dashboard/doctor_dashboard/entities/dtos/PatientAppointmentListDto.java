package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientAppointmentListDto {
    private Long appointId;
    private String category;
    private LocalDate dateOfAppointment;
    private LocalTime appointmentTime;
    private String doctorName;
    private String status;
    private Boolean isBookedAgain;

}
