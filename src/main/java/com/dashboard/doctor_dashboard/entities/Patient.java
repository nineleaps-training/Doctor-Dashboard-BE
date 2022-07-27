package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"squid:S5843","squid:S5869"})
@NoArgsConstructor
@Entity
@Table(
        name = "patient_details",indexes = @Index(name = "index_loginId",columnList = "login_id")
)
@SQLDelete(sql = "UPDATE login_details SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class    Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pID;

    @NotEmpty
    @Pattern(regexp = "^(Male|Female|Others)",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Enter Correct Gender!!")
    @Column(columnDefinition = "varchar(10)")
    private String gender;


    @Positive(message = "Age can't be null or less than equal to 0")
    private int age;

    @NotEmpty
    @Size(min = 10, max = 10, message = "Phone Number should Contains only 10 digits")
    @Column(columnDefinition = "varchar(10)")
    private String mobileNo;


    @NotEmpty
    @Pattern(regexp = "^(O-|O[+]|A-|B-|A[+]|AB-|B[+]|AB[+])", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Column(columnDefinition = "varchar(3)")
    private String bloodGroup;

    private String address;

    @Column(columnDefinition = "varchar(10)")
    private String alternateMobileNo;

    private boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments;


    @JsonBackReference()
    @OneToOne()
    @JoinColumn(name = "login_id",unique = true)
    private LoginDetails loginDetails;


    public Long getPID() {
        return pID;
    }

    public void setPID(Long pID) {
        this.pID = pID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

}
