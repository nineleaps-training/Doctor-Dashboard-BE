package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UpdatePrescriptionDto {
    private PatientDto patientDto;
    private List<Prescription> prescriptions;
    @NotNull
    @NotEmpty
    private String status;


    @NotNull
    @NotEmpty
    private String notes;

    private Boolean isBookedAgain;

    private Long followUpAppointmentId;

}
