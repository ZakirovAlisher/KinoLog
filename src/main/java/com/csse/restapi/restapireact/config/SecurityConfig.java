package com.csse.restapi.restapireact.config;



import com.csse.restapi.restapireact.filters.JwtRequestFilter;
import com.csse.restapi.restapireact.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserService userService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeRequests().antMatchers("/auth","/api/checkqr/{qr}", "/api/register","/api/reserve", "/api/profile", "/api/updProfile", "/api/addschedule", "/api/allschedules","/api/getschedule/{id}", "/api/saveschedule","/api/changePass", "/api/addmovie" , "/api/allmovies/{title}/{page}/{size}" , "/api/savemovie", "/api/getmovie/{id}", "/api/deletemovie","/api/deleteschedule" ,"/api/adduser" , "/api/allusers" , "/api/saveuser", "/api/getuser/{id}", "/api/deleteuser" ,  "/api/saveuserinfo","/api/allroles", "/api/assignrole", "/api/assigncat","/api/allmovies2", "/api/schedules/{search}","/api/allcategories","/api/addcategory","/api/savecategory", "/api/getcategory/{id}", "/api/deletecategory","/api/getschedulenotadm/{id}","/api/addhall","/api/allhalls","/api/gethall/{id}", "/api/deletehall", "/api/savehall").permitAll().
                anyRequest().authenticated().and().
                exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}