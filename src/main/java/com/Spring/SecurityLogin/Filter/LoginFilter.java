package com.Spring.SecurityLogin.Filter;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest= (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if(isAuthenticated() && ("/login".equals(servletRequest.getRequestURI()) || "/oauth2/**".equals(servletRequest.getRequestURI()) )){
            String encodedRedirectUrl= ((HttpServletResponse) response).encodeRedirectURL(servletRequest.getContextPath()+"/user");
            servletResponse.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
//            System.out.println("url= "+encodedRedirectUrl);
            servletResponse.setHeader("Location",encodedRedirectUrl);
        }
        chain.doFilter(servletRequest,servletResponse);
    }

    private boolean isAuthenticated(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            return false;
        }
        return authentication.isAuthenticated();
    }
}
