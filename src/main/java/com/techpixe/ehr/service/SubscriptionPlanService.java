package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.SubscriptionPlan;

import java.util.List;


public interface SubscriptionPlanService {

    SubscriptionPlan createSubscriptionPlan(Long userId, SubscriptionPlan subscriptionPlan);

    SubscriptionPlan upgradSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan);

 
}
