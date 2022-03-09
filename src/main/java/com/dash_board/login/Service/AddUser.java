package com.dash_board.login.Service;

import com.dash_board.login.Entity.DoctorLoginDetails;
import com.dash_board.login.Repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddUser {
    @Autowired
    private LoginRepo loginRepo;
     public void addDetails( Map<String ,Object> loginDetails){
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
