package com.techpixe.ehr.interceptor;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.exceptionhandle.PaymentRequiredException;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class SubscriptionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("user_Id");

        if (userId != null) {
            Optional<HR> userOptional = userRepository.findById(Long.valueOf(userId));
            // System.err.println(userOptional.get().isActive());
            if (userOptional.isPresent() && !userOptional.get().isActive()) {


              //  response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "User subscription has expired.Please Upgrade your subscription.");
            	throw new PaymentRequiredException("User subscription has expired.Please Upgrade your subscription.");
              // return false;
            }
        }
        String employeeId = request.getHeader("Id");
        if (employeeId != null) {

            Optional<EmployeeTable> employeeTableOptional = employeeTableRepository.findById(Long.valueOf(employeeId));
            if (employeeTableOptional.isPresent() && !employeeTableOptional.get().isActive()) {
                //response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "Employee subscription has expired.Please Upgrade your subscription.");
            	throw new PaymentRequiredException("Employee subscription has expired.Please Upgrade your subscription.");
             //  return false;
            }
        }


        return true;
    }
}
