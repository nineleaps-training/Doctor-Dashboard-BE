package com.Spring.SecurityLogin.Repository;

import com.Spring.SecurityLogin.Entity.DoctorLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepo extends JpaRepository<DoctorLoginDetails,Long> {
    public DoctorLoginDetails findByEmailId(String email);

}
