package com.sagar.Dashboard.controller;
import com.sagar.Dashboard.entity.DoctorClaims;
import com.sagar.Dashboard.entity.User;
import com.sagar.Dashboard.payload.Login;
import com.sagar.Dashboard.payload.AuthenticationResponse;
import com.sagar.Dashboard.payload.SignUp;
import com.sagar.Dashboard.repository.UserRepository;
import com.sagar.Dashboard.security.CustomUserDetailsService;
import com.sagar.Dashboard.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody Login login){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getUsername(),login.getEmail()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        // get token form tokenProvider

        DoctorClaims doctorClaims = new DoctorClaims();
        doctorClaims.setDoctorEmail(login.getEmail());
        doctorClaims.setDoctorName(login.getUsername());
        doctorClaims.setDoctorId(login.getId());


        String token = jwtTokenProvider.generateToken(authentication,doctorClaims);

        return ResponseEntity.ok(new AuthenticationResponse(token).getAccessToken());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUp signUp){

        // add check for username exists in a DB
        if(userRepository.existsByUsername(signUp.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUp.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        User user = new User();
        user.setName(signUp.getName());
        user.setUsername(signUp.getUsername());
        user.setEmail(signUp.getEmail());
        user.setPassword(signUp.getPassword());

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }


}



