package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(
        name = "patient_prescriptions",indexes = @Index(name = "index_appointId",columnList = "appointment_id")
)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presId;

    @Column(columnDefinition = "varchar(50)")
    private String drugName;
    private Long quantity;
    @Column(columnDefinition = "varchar(10)")
    private String type;
    private Long days;
    @Column(columnDefinition = "varchar(10)")
    private String time;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonBackReference("prescription")
    @ManyToOne()
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    public Prescription(String drugName, Long quantity, String type, Long days, String time) {
        this.drugName = drugName;
        this.quantity = quantity;
        this.type = type;
        this.days = days;
        this.time = time;
    }


}