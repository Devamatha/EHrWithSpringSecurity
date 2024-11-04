package com.techpixe.ehr.cleaner;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.SubscriptionPlan;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.SubscriptionPlanRepository;
import com.techpixe.ehr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionCleaner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private EmployeeTableRepository employeeTableRepository;
    // @Scheduled(cron = "0 0 0 0 0 ?")

    @Scheduled(cron = "0 50 14 * * ?")

    public void deactivateExpiredSubscriptions() {

        List<Long> users = userRepository.getAllUserIds();

        System.err.println("Subscription plan" + users);
        for (Long userId : users) {
            // System.err.println(userId+"user id");
            HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(userId + " User not found"));
            // System.err.println(user+"user");
            Long subscriptions = subscriptionPlanRepository.findLatestSubscriptionIdByUserId(user.getUser_Id());
            //System.err.println(subscriptions+"subscriptions");

            SubscriptionPlan latestSubscription = subscriptionPlanRepository.findById(subscriptions).orElseThrow(() -> new RuntimeException(subscriptions + " Subscription plan not found"));
            // System.err.println(latestSubscription+"latestSubscription");
            if (latestSubscription.getEndDate() != null && latestSubscription.getEndDate().isBefore(LocalDate.now())) {
                // System.out.println("inside if block");
                user.setActive(false);
                userRepository.save(user);
                List<EmployeeTable> employeeTables = user.getEmployeeTables();
                for (EmployeeTable employeeTable : employeeTables) {
                    employeeTable.setActive(false);
                    employeeTableRepository.save(employeeTable);
                }

            }


        }


    }
}
