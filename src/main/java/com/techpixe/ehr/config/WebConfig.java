package com.techpixe.ehr.config;

import com.techpixe.ehr.interceptor.SubscriptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SubscriptionInterceptor subscriptionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(subscriptionInterceptor)
                .addPathPatterns("/api/JobDetails/addJob/**", "/api/JobDetails/update/**", "/api/JobDetails/delete/**",
                        "/api/employees/user/**", "/api/employees/update/**", "/api/employees/delete/**",
                        "/api/holidays/user/**", "/api/holidays/update/**", "/api/holidays/delete/**",

                        "/api/notifications/send/**", "/api/payheads/user/**", "/api/candidates/upload-resume/**", "/api/candidates/update-Details/**",
                        "/api/subscriptions/save/**", "/api/addPayHeadsToEmployee/employeeData/**",
                        "/api/payHeads/user/**", "/api/payHeads/delete/**", "/api/payHeads/update/**", "/api/leaveApproval/status/**",
                        "/api/attendance/employee/**", "/api/attendance/update/**",
                        "/api/leaveApproval/employee/**"
                )
                .excludePathPatterns("/login", "/register", "api/contacts/save");  // Exclude paths where you don't need checks
    }
}
