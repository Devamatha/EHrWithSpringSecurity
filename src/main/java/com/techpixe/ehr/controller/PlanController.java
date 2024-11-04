package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.Plan;
import com.techpixe.ehr.service.AdminService;
import com.techpixe.ehr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/plan")
@RestController
public class PlanController {

    @Autowired
    private PlanService subscriptionPlanService;

    @Autowired
    private AdminService adminService;


    @PostMapping("/save/{adminId}")
    public ResponseEntity<Plan> createSubscriptionPlan(@RequestBody Plan subscriptionPlan, @PathVariable long adminId) {
        Plan createdPlan = subscriptionPlanService.createSubscriptionPlan(subscriptionPlan, adminId);
        return ResponseEntity.ok(createdPlan);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Plan> updateSubscriptionPlan(
            @PathVariable Long id,
            @RequestBody Plan subscriptionPlan) {
        Plan updatedPlan = subscriptionPlanService.updateSubscriptionPlan(id, subscriptionPlan);
        if (updatedPlan != null) {
            return ResponseEntity.ok(updatedPlan);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Plan> getSubscriptionPlanById(@PathVariable Long id) {
        Plan plan = subscriptionPlanService.getSubscriptionPlanById(id);
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
    public ResponseEntity<List<Plan>> getAllSubscriptionPlans() {
        List<Plan> plans = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(plans);
    }


}
