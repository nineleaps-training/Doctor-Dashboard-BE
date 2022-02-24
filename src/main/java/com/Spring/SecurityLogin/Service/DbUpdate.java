package com.Spring.SecurityLogin.Service;

import com.Spring.SecurityLogin.Entity.DoctorLoginDetails;
import com.Spring.SecurityLogin.Repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DbUpdate {
    @Autowired
    private  LoginRepo loginRepo;
     public void updateDetails( Map<String ,Object> loginDetails){
         DoctorLoginDetails doctorLoginDetails=loginRepo.findByEmailId(loginDetails.get("email").toString());
         if(doctorLoginDetails==null){
             DoctorLoginDetails newDoctor = new DoctorLoginDetails();
             newDoctor.setFirstName(loginDetails.get("given_name").toString());
             newDoctor.setLastName(loginDetails.get("family_name").toString());
             newDoctor.setDomain(loginDetails.get("hd").toString());
             newDoctor.setEmailId(loginDetails.get("email").toString());
//             newDoctor.setProfileId(loginDetails.get("picture").toString());
             loginRepo.save(newDoctor);
             System.out.println("User added");
         }
         else {
             System.out.println("Existing user");
         }
     }
}
