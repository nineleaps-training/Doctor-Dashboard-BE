package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUpDto {
    private Long appointId;
    private String doctorName;
    private Long doctorId;
    private String category;
    private String status;

}
