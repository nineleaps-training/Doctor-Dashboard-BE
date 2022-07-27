package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePrescriptionDto {
    @NotNull(message = "patient details can't be empty")
    private PatientDto patientDto;

    private List<Prescription> prescriptions;

    @NotNull
    @NotEmpty(message = "status can't be empty")
    private String status;


    @NotNull
    @NotEmpty(message = "notes can't be empty")
    private String notes;

    private Boolean isBookedAgain;

    private Long followUpAppointmentId;


}
