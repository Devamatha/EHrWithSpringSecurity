package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Admin;
import com.techpixe.ehr.entity.Plan;
import com.techpixe.ehr.repository.AdminRepository;
import com.techpixe.ehr.repository.PlanRepository;
import com.techpixe.ehr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    PlanRepository subscriptionPlanRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Plan createSubscriptionPlan(Plan subscriptionPlan, long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("adminId is not present"));
        subscriptionPlan.setAdmin(admin);
        return subscriptionPlanRepository.save(subscriptionPlan);
    }

    @Override
    public Plan updateSubscriptionPlan(Long id, Plan subscriptionPlan) {
        Optional<Plan> existingPlan = subscriptionPlanRepository.findById(id);
        if (existingPlan.isPresent()) {
            Plan planToUpdate = existingPlan.get();
            planToUpdate.setPlanType(subscriptionPlan.getPlanType());
            planToUpdate.setAmount(subscriptionPlan.getAmount());

            planToUpdate.setAdditionalFeatures(subscriptionPlan.getAdditionalFeatures());
            planToUpdate.setDiscription(subscriptionPlan.getDiscription());
            subscriptionPlanRepository.save(planToUpdate);
        } else {
            System.err.println("id is not present");
        }
        return null;
    }

    @Override
    public Plan getSubscriptionPlanById(Long id) {
        return subscriptionPlanRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSubscriptionPlan(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }

    @Override
    public List<Plan> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll();
    }

}
