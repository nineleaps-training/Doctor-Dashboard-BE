package com.dash_board.login.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public interface LoginService {
    public void addUser( Map<String ,Object> loginDetails);
    public void tokenVerification(String idTokenString) throws GeneralSecurityException, IOException;
}
