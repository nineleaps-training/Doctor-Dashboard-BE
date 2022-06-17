package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "select appoint_id from appointments where appoint_id=:appointmentId ", nativeQuery = true)
    Long getId(Long appointmentId);

    @Query(value = "select * from appointments where patient_id = :patientId",nativeQuery = true)
    List<Appointment> getAllAppointmentByPatientId(Long patientId);


    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment < curdate()",nativeQuery = true)
    List<Appointment> pastAppointment(Long patientId);

    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment = curdate() and appointment_time >= time(now())",nativeQuery = true)
    List<Appointment> todayAppointment1(Long patientId);

    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment = curdate() and appointment_time < time(now())",nativeQuery = true)
    List<Appointment> todayAppointment2(Long patientId);


    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment > curdate()",nativeQuery = true)
    List<Appointment> upcomingAppointment(Long patientId);

    @Query(value = "select appointment_time from appointments where doctor_id=:doctorId and date_of_appointment=:date",nativeQuery = true)
    List<Time>getTimesByIdAndDate(LocalDate date, Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId",nativeQuery = true)
    List<Appointment> getAllAppointmentByDoctorId(Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId order by timestamp desc limit 3", nativeQuery = true)
    List<Appointment> recentAppointment(Long doctorId);

    @Query(value = "select * from appointments where appoint_id = :appointId",nativeQuery = true)
    Appointment getAppointmentById(Long appointId);

    @Query(value = "Select date_of_appointment from appointments where doctor_id =:doctorId", nativeQuery = true)
    ArrayList<Date> getAllDatesByDoctorId(@Param(value = "doctorId") Long doctorId);
//=======
    @Query(value = "Select COUNT(appoint_id) from appointments where doctor_id =:doctorId", nativeQuery = true)
    int totalNoOfAppointment(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select count(appoint_id) from appointments where doctor_id=:doctorId and week(timestamp)=week(now())", nativeQuery = true)
    int totalNoOfAppointmentAddedThisWeek(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment < curdate()",nativeQuery = true)
    List<Appointment> receptionistDoctorAppointment(Long doctorId);

}