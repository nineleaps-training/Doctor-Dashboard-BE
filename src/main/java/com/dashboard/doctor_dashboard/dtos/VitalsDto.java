package com.dashboard.doctor_dashboard.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
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
