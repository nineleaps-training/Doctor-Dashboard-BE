package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewDto {
    private String name;
    private String email;
    private String gender;
    private String speciality;

    private short age;
    private String bloodGroup;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;
    private Attributes attributes;
    private String status;
    private List<Prescription> prescription;

    public AppointmentViewDto(String name, String speciality, LocalDate dateOfAppointment, LocalTime timeOfAppointment, String status,String bloodGroup,short age,String gender) {
        this.name = name;
        this.speciality = speciality;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
        this.status = status;
        this.bloodGroup=bloodGroup;
        this.gender=gender;
        this.age=age;
    }

}

