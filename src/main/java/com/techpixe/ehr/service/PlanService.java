package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Plan;

import java.util.List;


public interface PlanService {

    Plan createSubscriptionPlan(Plan subscriptionPlan, long adminId);

    Plan updateSubscriptionPlan(Long id, Plan subscriptionPlan);

    Plan getSubscriptionPlanById(Long id);

    List<Plan> getAllSubscriptionPlans();

    void deleteSubscriptionPlan(Long id);


}
