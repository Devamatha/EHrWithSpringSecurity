package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.Plan;
import com.techpixe.ehr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/plan")
@RestController
public class PlanController {

	@Autowired
	private PlanService planService;

	@PostMapping("/save/{adminId}")
	public ResponseEntity<Map<String, Object>> createSubscriptionPlan(@RequestBody Plan subscriptionPlan,
			@PathVariable long adminId) throws Exception {
		try {
			planService.createSubscriptionPlan(subscriptionPlan, adminId);
			return ResponseEntity.ok(Collections.singletonMap("message", "Plan created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<Map<String, Object>> updateSubscriptionPlan(@PathVariable Long id,
			@RequestBody Plan subscriptionPlan) throws Exception {
		try {
			planService.updateSubscriptionPlan(id, subscriptionPlan);
			return ResponseEntity.ok(Collections.singletonMap("message", "Plan updated successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
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
	public void deleteSubscriptionPlan(@PathVariable Long id) throws Exception {
		try {
			planService.deleteSubscriptionPlan(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Plan>> getAllSubscriptionPlans() {
		List<Plan> plans = planService.getAllSubscriptionPlans();
		return ResponseEntity.ok(plans);
	}

}
