package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "select appoint_id from appointments where id=:appointmentId ", nativeQuery = true)
    Long getId(Long appointmentId);

    @Query(value = "select * from appointments where patient_id = :patientId",nativeQuery = true)
    List<Appointment> getAllAppointmentByPatientId(Long patientId);

    @Query(value = "select * from appointments where doctor_id = :doctorId",nativeQuery = true)
    List<Appointment> getAllAppointmentByDoctorId(Long doctorId);

    @Query(value = "select * from appointments where appoint_id = :appointId",nativeQuery = true)
    Appointment getAppointmentById(Long appointId);

    @Query(value = "Select date_of_appointment from appointments where doctor_id =:doctorId", nativeQuery = true)
    ArrayList<Date> getAllDatesByDoctorId(@Param(value = "doctorId") Long doctorId);

}
