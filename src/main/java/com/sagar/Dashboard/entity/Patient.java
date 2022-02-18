package com.sagar.Dashboard.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "patients"
)
public class Patient {

    @Id
    @GeneratedValue(generator = "patient_sequence")
    @SequenceGenerator(name = "patient_sequence",initialValue = 1,allocationSize = 100)
    @Column(name = "id")
    private Long pID;
    private String fullName;
    private String emailId;
    private Date dateOfBirth;
    private String gender;
    private int age;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Attributes attributes;


    @Override
    public String toString() {
        return "Patient{" +
                "pID=" + pID +
                ", fullName='" + fullName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", attributes=" + attributes +
                '}';
    }
}
