package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDto {

    private Long appointId;
    private LocalDate dateOfAppointment;
    private String patientName;
    private String patientEmail;
    private String symptoms;
    private String category;

    private Boolean isBookedAgain;
    private Long followUpAppointmentId;
    private Attributes attributes;
    private PatientEntityDto patient;
    private List<Prescription> prescription;
    private String status;


}

