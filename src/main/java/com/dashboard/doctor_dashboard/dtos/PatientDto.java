package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientDto {

    private String category;
    private String doctorName;
    private String status;
    private String patientName;
    private String patientEmail;
    private int age;
    private String gender;

    @Override
    public String toString() {
        return "PatientDto{" +
                "category='" + category + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", status='" + status + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientEmail='" + patientEmail + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
