package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.AuthenticationResponse;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of JWT Service interface
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    /**
     * @param login this variable contains login details.
     * @return it returns
     */
    public String authenticateUser(Login login) {
        log.info("inside: JwtServiceImpl::authenticateUser");
        String roles = login.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles));
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getUsername(),authorities));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        // get token form tokenProvider

        var claims = new Claims();
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setDoctorId(login.getId());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        String token = jwtTokenProvider.generateToken(authentication, claims);
        log.info("JwtServiceImpl::JWT Token created");
        log.info("exit: JwtServiceImpl::authenticateUser");
        return new AuthenticationResponse(token).getAccessToken();
    }


}



