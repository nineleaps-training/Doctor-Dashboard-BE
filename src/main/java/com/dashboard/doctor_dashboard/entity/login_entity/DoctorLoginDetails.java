package com.dashboard.doctor_dashboard.entity.login_entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_login_details", uniqueConstraints = @UniqueConstraint(columnNames = {"emailId"}))
public class DoctorLoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    private String lastName;
    @Column(name = "emailId", nullable = false, unique = true)
    private String emailId;
    @Column(name = "domain", nullable = false)
    private String domain;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}