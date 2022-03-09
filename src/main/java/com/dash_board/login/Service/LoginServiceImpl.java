package com.dash_board.login.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.dash_board.login.Entity.DoctorLoginDetails;
import com.dash_board.login.Repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepo loginRepo ;

    public void addUser( Map<String ,Object> loginDetails){
         System.out.println("email="+loginDetails.get("email"));
         System.out.println("val="+loginRepo.findByEmailId(loginDetails.get("email").toString()));
         DoctorLoginDetails doctorLoginDetails=loginRepo.findByEmailId(loginDetails.get("email").toString());
         if(doctorLoginDetails==null){
             DoctorLoginDetails newDoctor = new DoctorLoginDetails();
             newDoctor.setFirstName(loginDetails.get("given_name").toString());
             newDoctor.setLastName(loginDetails.get("family_name").toString());
             newDoctor.setDomain(loginDetails.get("hd").toString());
             newDoctor.setEmailId(loginDetails.get("email").toString());
             loginRepo.save(newDoctor);
             System.out.println("User added");
         }
         else {
             System.out.println("Existing user");
         }
     }

     //Token verification
    public void tokenVerification(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("66297814659-gkj68lfu116ai19tb6e2rfacqt9bja0s.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            Payload payload = idToken.getPayload();
            addUser(payload);
        }
        else {
            System.out.println("Invalid ID token.");
        }
    }
}
