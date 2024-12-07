package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.SubscriptionPlan;
import com.techpixe.ehr.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/subscriptions")
@RestController
public class SubscriptionPlanController {

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@PostMapping("/save/{userId}")
	public ResponseEntity<Map<String, String>> createSubscriptionPlan(@PathVariable long userId,
			@RequestBody SubscriptionPlan subscriptionPlan) throws Exception {
		try {
			subscriptionPlanService.createSubscriptionPlan(userId, subscriptionPlan);
			return ResponseEntity.ok(Collections.singletonMap("message", "SubscriptionPlan created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PostMapping("/upgrade/{userId}")
	public ResponseEntity<Map<String, String>> upgradeSubscriptionPlan(@PathVariable long userId,
			@RequestBody SubscriptionPlan subscriptionPlan) throws Exception {
		try {
			subscriptionPlanService.upgradSubscriptionPlan(userId, subscriptionPlan);
			return ResponseEntity.ok(Collections.singletonMap("message", "SubscriptionPlan updated successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	

}
