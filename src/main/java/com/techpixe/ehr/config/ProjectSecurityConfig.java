package com.techpixe.ehr.config;


import com.techpixe.ehr.exceptionhandle.CustomAccessDeniedHandler;
import com.techpixe.ehr.exceptionhandle.CustomBasicAuthenticationEntryPoint;
import com.techpixe.ehr.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import com.techpixe.ehr.filter.RequestValidationBeforeFilter;

        

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/api/users/user", "/api/clients/login", "/api/clients/save",
                                "/api/plan/getAll","/api/users/add-job-details/{userId}",
                                "/api/users/holiday/{userId}","/api/users/employeedetails/{userId}",
                                "/api/users/payHeads/{userId}","/api/users/attendance/{userId}","/api/employees/{id}",
                                "/api/JobDetails/{id}","/api/employees/addPayHeaddetails/{id}","/api/payHeads/{payHeadId}")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(POST,"/api/plan/save/**").hasRole("ADMIN")
                        .requestMatchers("/api/clients/save/Employee/{id}",
                                "/api/JobDetails/addJob/{user_Id}","/api/JobDetails/{id}","/api/JobDetails/update/{jobId}","/api/JobDetails/delete/{id}",
                                "/api/users/add-job-details/{userId}","/api/users/holiday/{userId}",
                                "/api/holidays/user/{userId}","/api/holidays/update/{holidayId}","/api/holidays/delete/{id}","/api/holidays/{holidayId}",
                                "/api/users/employeedetails/{userId}","/api/users/payHeads/{userId}",
                                "/api/users/attendance/{userId}","/api/users/leave-approvals/{userId}",
                                "/api/users/personalInformation/{userId}","/api/candidates/upload-resume/{user_id}",
                                "/api/attendance/update/{id}",
                                "/api/payHeads/delete/{id}","/api/payHeads/{payHeadId}","/api/payHeads/update/{payHeadId}","/api/payHeads/user/{userId}",
                                "/api/employees/user/{userId}","/api/employees/{id}","/api/employees/update/{id}","/api/employees/addPayHeaddetails/{id}",
                                "/api/leaveApproval/status/{id}",
                                "/api/candidates/update-Details/{user_id}").hasRole("HR")
                        .requestMatchers( "/api/attendance/employee/{empId}","/api/attendance/employee/attendance/{empId}/{date}",
                                "/api/leaveApproval/employee/{empId}").hasRole("EMPLOYEE")
                        .requestMatchers("/api/users/allUsers").hasAnyRole("HR","ADMIN")

                        .requestMatchers("/api/users/user", "/api/clients/login", "/api/clients/save", "/api/plan/getAll").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        UserNamePwdAuthenticationProvider authenticationProvider =
                new UserNamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }

}