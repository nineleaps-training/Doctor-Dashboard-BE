package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowUpDto {
    private Long appointId;
    private String doctorName;
    private Long doctorId;
    private String category;
    private String status;


}
