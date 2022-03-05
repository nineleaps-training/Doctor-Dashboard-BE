package com.sagar.Doctor.Dashboard.repository;

import com.sagar.Doctor.Dashboard.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    @Query(value = "Select COUNT(id) from patients",nativeQuery = true)
     int totalNoOfPatient();

     @Query(value = "Select category,count(category) from patients group by category",nativeQuery = true)
     ArrayList<String> patientCategory();

    @Query(value = "Select gender,count(gender) from patients group by gender",nativeQuery = true)
     ArrayList<String> gender();

}
