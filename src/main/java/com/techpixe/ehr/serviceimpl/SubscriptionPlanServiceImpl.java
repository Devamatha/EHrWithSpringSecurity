package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.SubscriptionPlan;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.SubscriptionPlanRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeTableRepository employeeTableRepository;

    @Override
    public SubscriptionPlan createSubscriptionPlan(Long userId, SubscriptionPlan subscriptionPlan) {
        HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(userId + " is Not found "));
        subscriptionPlan.setUser(user);
        LocalDate registrationDate = LocalDate.now();
        LocalDate endDate = null;
        subscriptionPlan.setCreatedAt(LocalDate.now());
        subscriptionPlan.setPlanType(subscriptionPlan.getPlanType());
        subscriptionPlan.setAmount(subscriptionPlan.getAmount());
        subscriptionPlan.setTotalResumes(subscriptionPlan.getTotalResumes());
        subscriptionPlan.setStartDate(registrationDate);
        switch (subscriptionPlan.getPlanType()) {
            case "Quarterly":
                endDate = registrationDate.plusMonths(3);
                break;
            case "Monthly":
                endDate = registrationDate.plusMonths(1);
                break;
            case "Yearly":
                endDate = registrationDate.plusYears(1);
                break;
            case "LifeTime":
                break;
            default:
                throw new IllegalArgumentException("Invalid plan type");

        }
        subscriptionPlan.setEndDate(endDate);
        return subscriptionPlanRepository.save(subscriptionPlan);
    }

    @Override
    public SubscriptionPlan upgradSubscriptionPlan(Long userId, SubscriptionPlan subscriptionPlan) {
        HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(userId + " is Not found "));
        subscriptionPlan.setUser(user);
        LocalDate registrationDate = LocalDate.now();
        LocalDate endDate = null;
        subscriptionPlan.setCreatedAt(LocalDate.now());
        subscriptionPlan.setPlanType(subscriptionPlan.getPlanType());
        subscriptionPlan.setAmount(subscriptionPlan.getAmount());
        subscriptionPlan.setTotalResumes(subscriptionPlan.getTotalResumes());
        subscriptionPlan.setStartDate(registrationDate);
        switch (subscriptionPlan.getPlanType()) {
            case "Quarterly":
                endDate = registrationDate.plusMonths(3);
                break;
            case "Monthly":
                endDate = registrationDate.plusMonths(1);
                break;
            case "Yearly":
                endDate = registrationDate.plusYears(1);
                break;
            case "LifeTime":
                break;
            default:
                throw new IllegalArgumentException("Invalid plan type");

        }
        user.setActive(true);
        userRepository.save(user);
        List<EmployeeTable> employeeTables = user.getEmployeeTables();
        for (EmployeeTable employeeTable : employeeTables) {
            employeeTable.setActive(true);
            employeeTableRepository.save(employeeTable);
        }
        subscriptionPlan.setEndDate(endDate);
        return subscriptionPlanRepository.save(subscriptionPlan);
    }

    

}
