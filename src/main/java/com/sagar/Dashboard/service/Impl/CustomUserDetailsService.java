package com.sagar.Dashboard.service.Impl;

import com.sagar.Dashboard.login.CustomUserDetails;
import com.sagar.Dashboard.login.User;
import com.sagar.Dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        final User user = this.userRepository.findByUsername(userName);

        if(user == null)
        {
            throw new UsernameNotFoundException("User not found!!!");
        }else
        {
            return new CustomUserDetails(user);
        }

    }
}
