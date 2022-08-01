package com.dashboard.doctor_dashboard.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "patient_files")
public class FileDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] dataReport;

    @Column(unique = true)
    private Long appointmentId;

    public FileDB() {
    }

    public FileDB(String name, String type, byte[] dataReport, Long appointmentId) {
        this.name = name;
        this.type = type;
        this.dataReport = dataReport;
        this.appointmentId = appointmentId;
    }



}