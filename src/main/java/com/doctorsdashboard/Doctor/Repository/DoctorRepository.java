package com.doctorsdashboard.Doctor.Repository;

import com.doctorsdashboard.Doctor.Entity.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DoctorRepository extends JpaRepository<DoctorDetails,Long> {
    public List<DoctorDetails> findByName(String name);
    public List<DoctorDetails>  findByAge(short age);
    public DoctorDetails findByEmail(String email);
    public List<DoctorDetails>  findBySpeciality(String speciality);
}
