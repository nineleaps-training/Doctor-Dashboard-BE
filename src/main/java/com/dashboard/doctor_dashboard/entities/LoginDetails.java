package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "login_details",
        indexes = @Index(name = "index_email",columnList = "email_id"))
@SQLDelete(sql = "UPDATE login_details SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class LoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false,columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "email_id", nullable = false, unique = true,columnDefinition = "varchar(80)")
    private String emailId;
    @Column(name = "domain", nullable = false,columnDefinition = "varchar(30)")
    private String domain;
    @Column(name = "profile_pic",nullable = false,unique = true)
    private String profilePic;
    @Column(name="role",nullable = false,columnDefinition = "varchar(8)")
    private String role;

    private boolean deleted = Boolean.FALSE;
    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @OneToOne(mappedBy = "loginDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorDetails doctorDetails;


    @JsonManagedReference
    @OneToOne(mappedBy = "loginDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Patient patient;


}