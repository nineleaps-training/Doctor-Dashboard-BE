package com.dashboard.doctor_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details"
)
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private long id;
    @Column(name = "dName", nullable = false)
    private String name;
    @Column(name="dAge",nullable = false)
    private short age;
    @Column(name = "DEmail",nullable = false,unique = true)
    private String email;
    @Column(name="dSpecialty",nullable = false)
    private String speciality;


    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Patient> patient;


    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Todolist> todolist;

    //references
}
