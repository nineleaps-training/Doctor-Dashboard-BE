package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ValidationsSchema {
    private Date timestamp;
    private List<String> messages;
    private String details;


}
