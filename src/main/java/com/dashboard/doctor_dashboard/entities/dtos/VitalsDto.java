package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class VitalsDto {

    @NotNull
    private Long appointmentId;
    @NotNull
    private Long glucoseLevels;

    @NotNull
    private Double bodyTemperature;

    @NotNull
    @NotEmpty
    private String bloodPressure;


}
