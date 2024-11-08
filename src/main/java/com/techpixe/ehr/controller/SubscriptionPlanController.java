package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.entity.SubscriptionPlan;
import com.techpixe.ehr.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/subscriptions")
@RestController
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;




    @PostMapping("/save/{userId}")
    public ResponseEntity<SubscriptionPlan> createSubscriptionPlan(@PathVariable long userId, @RequestBody SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan createdPlan = subscriptionPlanService.createSubscriptionPlan(userId, subscriptionPlan);
        return ResponseEntity.ok(createdPlan);
    }

    @PostMapping("/upgrade/{userId}")
    public ResponseEntity<SubscriptionPlan> upgradeSubscriptionPlan(@PathVariable long userId, @RequestBody SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan createdPlan = subscriptionPlanService.upgradSubscriptionPlan(userId, subscriptionPlan);
        return ResponseEntity.ok(createdPlan);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<SubscriptionPlan> updateSubscriptionPlan(
            @PathVariable Long id,
            @RequestBody SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan updatedPlan = subscriptionPlanService.updateSubscriptionPlan(id, subscriptionPlan);
        if (updatedPlan != null) {
            return ResponseEntity.ok(updatedPlan);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SubscriptionPlan> getSubscriptionPlanById(@PathVariable Long id) {
        SubscriptionPlan plan = subscriptionPlanService.getSubscriptionPlanById(id);
        if (plan != null) {
            return ResponseEntity.ok(plan);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long id) {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SubscriptionPlan>> getAllSubscriptionPlans() {
        List<SubscriptionPlan> plans = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(plans);
    }





    private boolean isEmail(String emailOrMobileNumber) {
        return emailOrMobileNumber.contains("@");
    }

    private boolean isMobileNumber(String emailOrMobileNumber) {
        return emailOrMobileNumber.matches("\\d+");
    }

}
