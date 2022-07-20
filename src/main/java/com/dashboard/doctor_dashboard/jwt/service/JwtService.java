package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.Login;
import org.springframework.stereotype.Service;

/**
 * Interface of JWT Service layer
 */
@Service
public interface JwtService {

    String authenticateUser(Login login);
}
