package com.dashboard.doctor_dashboard.interceptor;

import com.dashboard.doctor_dashboard.exceptions.*;
import com.dashboard.doctor_dashboard.jwt.security.*;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.util.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    private JwtTokenProvider tokenProvider;

    private LoginRepo loginRepo;

    @Autowired
    public AuthenticationInterceptor(JwtTokenProvider tokenProvider, LoginRepo loginRepo) {
        this.tokenProvider = tokenProvider;
        this.loginRepo = loginRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            int userId = Integer.parseInt(request.getHeader("userId"));
            String jwtToken;
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                jwtToken = bearerToken.substring(7);
                tokenProvider.validateToken(jwtToken, request);
                String username = tokenProvider.getUsernameFromJWT(jwtToken);
                int id = loginRepo.getId(username);
                if (userId == id) {
                    log.debug("token successfully authenticated");
                    return true;
                }
                throw new APIException(Constants.UNAUTHORIZED);
            } else {
                throw new APIException("JWT claims string is empty.");
            }
        } catch (NumberFormatException e) {

            throw new APIException("userId is missing in header!!!");
        }
    }
}
