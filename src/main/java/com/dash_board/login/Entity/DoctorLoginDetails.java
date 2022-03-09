package com.dash_board.login.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_login_details",uniqueConstraints = @UniqueConstraint(columnNames = {"emailId"}))
public class DoctorLoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="firstName",nullable = false)
    private String firstName;
    private String lastName;
    @Column(name="emailId",nullable = false)
    private String emailId;

    private String domain;
    private String profileId;

}