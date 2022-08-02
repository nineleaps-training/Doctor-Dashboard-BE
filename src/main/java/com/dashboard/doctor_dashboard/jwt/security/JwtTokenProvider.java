package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // generate token
    public String generateToken(String email, Claims tokenClaims) {

        log.info("inside: JwtTokenProvider::generateToken");
        Map<String,Object> claims= new HashMap<>();
        claims.put("DoctorDetails",tokenClaims);
        log.info("exit: JwtTokenProvider::generateToken");
        return doGenerateToken(email,claims);

    }

    public String doGenerateToken(String email,Map<String,Object> claims){
        log.info("inside: JwtTokenProvider::doGenerateToken");

        var currentDate = new Date();
        var expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);
        log.info("exit: JwtTokenProvider::doGenerateToken");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // get username from the token
    public String getUsernameFromJWT(String token) {
        log.info("inside: JwtTokenProvider::getUsernameFromJWT");

        var claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        log.info("inside: JwtTokenProvider::getUsernameFromJWT");
        return claims.getSubject();
    }

    public Long getIdFromToken(HttpServletRequest request){
        log.info("inside: JwtTokenProvider::getIdFromToken");

        String jwtToken;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwtToken= bearerToken.substring(7, bearerToken.length());
            validateToken(jwtToken,request);
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            var mapper = new ModelMapper();
            Claims details = mapper.map(claims.get("DoctorDetails"), Claims.class);
            log.info("exit: JwtTokenProvider::getIdFromToken");
            return details.getDoctorId();
        }else{
            log.info(" JwtTokenProvider::getIdFromToken");
            throw new APIException("JWT claims string is empty.");
        }
    }
    // validate JWT token
    public boolean validateToken(String token,HttpServletRequest request) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException  |UnsupportedJwtException | IllegalArgumentException ex) {
            throw new APIException( "Invalid JWT token");
        }
        catch (ExpiredJwtException ex) {
            if(refreshToken(ex,request))
                return false;
            throw new APIException("Expired JWT token");
        }
    }

    boolean refreshToken(ExpiredJwtException ex,HttpServletRequest request){
        log.info("inside::refreshToken");
        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();
        if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh-token")) {
            allowForRefreshToken(ex, request);
            log.info("exit1::refreshToken");
            return true;
        } else {
            request.setAttribute("exception", ex);
            log.info("exit2::refreshToken");
            return false;
        }
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        log.info("inside::allowForRefreshToken");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims", ex.getClaims());
        log.info("exit::allowForRefreshToken");

    }


}
