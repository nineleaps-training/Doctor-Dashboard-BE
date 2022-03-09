package com.dash_board.login.Repository;

import com.dash_board.login.Entity.DoctorLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepo extends JpaRepository<DoctorLoginDetails,Long> {
    public DoctorLoginDetails findByEmailId(String email);

}
