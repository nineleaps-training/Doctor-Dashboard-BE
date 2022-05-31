package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.entities.login_entity.DoctorLoginDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DoctorDetails {
    @Id
    private Long id;
    @Column(name = "age",nullable = false)
    @Range(min = 24, max = 100, message = "enter age between 24-100")
    private Short age;
    @Column(name = "speciality",nullable = false)
    private String speciality;

    @Column(name = "phone_no",nullable = false)
    private String phoneNo;
    @Column(name = "gender",nullable = false)
    private String gender;
    //references
    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patient;


    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Todolist> todolist;


    @OneToOne()
    @JoinColumn(name = "login_id",referencedColumnName = "id",nullable = false)
    private DoctorLoginDetails doctorLoginDetails;

    @Column(name = "login_id", insertable=false, updatable=false,nullable = false)
    private Long loginId;



    public Long getLoginId() {
        return loginId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public Short getAge() {
        return age;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

//    public DoctorDetails(Short age, String speciality, String phoneNo, String gender, Long loginId) {
//        this.age = age;
//        this.speciality = speciality;
//        this.phoneNo = phoneNo;
//        this.gender = gender;
//        this.loginId = loginId;
//    }


    @Override
    public String toString() {
        return "DoctorDetails{" +
                "id=" + id +
                ", age=" + age +
                ", speciality='" + speciality + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                ", patient=" + patient +
                ", todolist=" + todolist +
                ", doctorLoginDetails=" + doctorLoginDetails +
                ", loginId=" + loginId +
                '}';
    }
}

