package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
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
    private Gender gender;
    private Category speciality;

    private short age;
    private BloodGroup bloodGroup;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;
    private Attributes attributes;
    private String status;
    private List<Prescription> prescription;
    @SuppressWarnings("squid:S107")
    public AppointmentViewDto(String name, Category speciality, LocalDate dateOfAppointment, LocalTime timeOfAppointment, String status,BloodGroup bloodGroup,short age,Gender gender) {
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

