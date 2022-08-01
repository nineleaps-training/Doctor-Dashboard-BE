package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {

    @Query(value = "select * from patient_details where deleted = false and login_id=:loginId", nativeQuery = true)
    Patient getPatientByLoginId(Long loginId);

    @Query(value = "select id from patient_details where deleted = false and login_id=:loginId ", nativeQuery = true)
    Long getId(Long loginId);

    @Query(value = "Select gender,count(gender) from patient_details where deleted = false and doctor_id =:doctorId group by gender", nativeQuery = true)
    ArrayList<String> gender(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select status,count(status) from patient_details where deleted = false and doctor_id =:doctorId group by status", nativeQuery = true)
    ArrayList<String> activePatient(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select upper(blood_group),count(blood_group) from attributes inner join patient_details where deleted = false and patient_details.id = attributes.id and doctor_id =:doctorId group by blood_group", nativeQuery = true)
    ArrayList<String> bloodGroup(@Param(value = "doctorId") Long doctorId);

    @Query(value = "UPDATE patient_details SET deleted = true WHERE id=:id", nativeQuery = true)
    Void deletePatientById(Long id);

    @Query(value = "insert into patient_details (age,mobile_no,alternate_mobile_no,timestamp,gender,address,blood_group,created_at,login_id) values(:age,:mobileNo,:alternateMobileNo,now(),:gender,:address,:bloodGroup,now(),:loginId)",nativeQuery = true)
    @Transactional
    @Modifying
    void insertIntoPatient(int age,String mobileNo,String alternateMobileNo,String gender,String address,String bloodGroup,Long loginId);

    @Query(value = "update patient_details set mobile_no=:mobileNo where deleted = false and id=:patientId",nativeQuery = true)
    @Modifying
    @Transactional
    void updateMobileNo(String mobileNo,long patientId);

}