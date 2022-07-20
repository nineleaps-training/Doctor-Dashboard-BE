package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.Login;
import org.springframework.stereotype.Service;

/**
 * interface for Jwt service layer.
 */
@Service
public interface JwtService {

    String authenticateUser(Login login);
}
