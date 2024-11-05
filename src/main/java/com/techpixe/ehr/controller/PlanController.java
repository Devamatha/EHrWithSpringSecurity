package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.Plan;
import com.techpixe.ehr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/plan")
@RestController
public class PlanController {

    @Autowired
    private PlanService planService;




    @PostMapping("/save/{adminId}")
    public ResponseEntity<Plan> createSubscriptionPlan(@RequestBody Plan subscriptionPlan, @PathVariable long adminId) {
        Plan createdPlan = planService.createSubscriptionPlan(subscriptionPlan, adminId);
        return ResponseEntity.ok(createdPlan);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Plan> updateSubscriptionPlan(
            @PathVariable Long id,
            @RequestBody Plan subscriptionPlan) {
        Plan updatedPlan = planService.updateSubscriptionPlan(id, subscriptionPlan);
        if (updatedPlan != null) {
            return ResponseEntity.ok(updatedPlan);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Plan> getSubscriptionPlanById(@PathVariable Long id) {
        Plan plan = planService.getSubscriptionPlanById(id);
        if (plan != null) {
            return ResponseEntity.ok(plan);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long id) {
        planService.deleteSubscriptionPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Plan>> getAllSubscriptionPlans() {
        List<Plan> plans = planService.getAllSubscriptionPlans();
        return ResponseEntity.ok(plans);
    }


}
