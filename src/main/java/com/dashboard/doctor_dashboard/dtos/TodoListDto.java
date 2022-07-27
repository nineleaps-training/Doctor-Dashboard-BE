package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoListDto {

    @NotNull(message = "description can't be null")
    @NotEmpty(message = "description cant't be Empty")
    private String description;
    @NotNull(message = "status cant'be null")
    private Boolean status;
    @NotNull(message = "doctordetails cant'be null")
    private DoctorDetails doctorDetails;


}
