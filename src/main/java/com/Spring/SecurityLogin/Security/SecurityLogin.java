package com.Spring.SecurityLogin.Security;

import com.Spring.SecurityLogin.Filter.LoginFilter;
import com.Spring.SecurityLogin.Security.OAuth.CustomOAuth2User;
import com.Spring.SecurityLogin.Security.OAuth.CustomOAuth2UserImplementation;
import com.Spring.SecurityLogin.Service.DbUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityLogin extends WebSecurityConfigurerAdapter {


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("pranay")
//                .password("1234")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("4321")
//                .roles("USER");
//
//
//    }
//    @Bean
//    public PasswordEncoder getPasswordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Override

    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterAfter(new LoginFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
                .antMatchers("/login","/","/oauth2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                        .userService(customOAuth2UserImplementation)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        CustomOAuth2User OAuth2User= (CustomOAuth2User) authentication.getPrincipal();
                        dbUpdate.updateDetails(OAuth2User.getAttributes());
                        System.out.println("Current User "+OAuth2User.getAttributes());
                        response.sendRedirect("/user");
                    }
                });


    }
    @Autowired
   private CustomOAuth2UserImplementation customOAuth2UserImplementation;
    @Autowired
    private DbUpdate dbUpdate;
}
