package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Clients;
import com.techpixe.ehr.entity.Plan;
import com.techpixe.ehr.repository.ClientsRepository;
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
    ClientsRepository clientsRepository;

    @Override
    public Plan createSubscriptionPlan(Plan subscriptionPlan, long adminId) {
        Clients cleints = clientsRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("adminId is not present"));
        subscriptionPlan.setClients(cleints);
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
