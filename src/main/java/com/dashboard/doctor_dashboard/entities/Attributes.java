package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(
        name = "patient_attributes",indexes = @Index(name = "index_appointment",columnList = "appointment_id")
)
public class Attributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aID;
    @Column(columnDefinition = "varchar(10)")
    private String  bloodPressure;
    private Long glucoseLevel;
    private Double bodyTemp;
    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(columnDefinition = "varchar(100)")
    private String prescription;


    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;





}