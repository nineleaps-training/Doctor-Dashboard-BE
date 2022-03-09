package com.dash_board.login.Repository;

import com.dash_board.login.Entity.DoctorLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends JpaRepository<DoctorLoginDetails,Long> {
    DoctorLoginDetails findByEmailId(String email);

    @Query(value = "select id from doctor_login_details l where l.email_id =:email",nativeQuery = true)
    int getId(String email);

}
