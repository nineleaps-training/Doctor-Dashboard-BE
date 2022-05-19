package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.login_entity.DoctorLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<DoctorLoginDetails, Long> {
    DoctorLoginDetails findByEmailId(String email);

    @Query(value = "select id from doctor_login_details l where l.email_id =:email", nativeQuery = true)
    int getId(String email);

    @Query(value = "select id from doctor_login_details d where d.id=:id", nativeQuery = true)
    Long isIdAvailable(Long id);

    Optional<DoctorLoginDetails> findByFirstNameOrEmailId(String usernameOrEmail, String username);
}
