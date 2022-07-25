package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
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


    public Long getAID() {
        return aID;
    }

    public void setAID(Long aID) {
        this.aID = aID;
    }

    public String  getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String  bloodPressure) {
        this.bloodPressure = bloodPressure;
    }


    public Long getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(Long glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public Double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(Double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }


}
