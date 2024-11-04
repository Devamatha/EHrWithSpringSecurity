package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.SubscriptionPlan;

import java.util.List;


public interface SubscriptionPlanService {

    SubscriptionPlan createSubscriptionPlan(Long userId, SubscriptionPlan subscriptionPlan);

    SubscriptionPlan upgradSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan);

    SubscriptionPlan updateSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan);


    SubscriptionPlan getSubscriptionPlanById(Long id);

    List<SubscriptionPlan> getAllSubscriptionPlans();

    void deleteSubscriptionPlan(Long id);

    //public boolean isSubscriptionActive(Long userId);
}
