package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * interface for Jwt service layer.
 */
@Service
public interface JwtService {

    String authenticateUser(Login login);
     String createRefreshToken(DefaultClaims claims);
}
